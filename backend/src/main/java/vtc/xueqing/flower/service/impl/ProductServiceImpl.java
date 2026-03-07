package vtc.xueqing.flower.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.Supplier;
import vtc.xueqing.flower.exception.BusinessException;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.mapper.MerchantMapper;
import vtc.xueqing.flower.mapper.SupplierMapper;
import vtc.xueqing.flower.service.ProductService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * Product service implementation.
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private MerchantMapper merchantMapper;

    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public Product createProduct(Product product) {
        // 1. Set default values
        product.setSales(0);
        product.setStatus(Constants.PRODUCT_STATUS_ACTIVE);
        product.setStockStatus(getStockStatus(product.getStock()));
        product.setCreateDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());

        // 2. Persist to database
        int result = productMapper.insert(product);
        if (result == 0) {
            throw new BusinessException("Failed to create product");
        }

        log.info("Product created successfully, productId: {}, name: {}", product.getProdId(), product.getName());
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        if (product.getProdId() == null) {
            throw new BusinessException("Product ID cannot be null");
        }

        // 1. Check whether the product exists
        Product existProduct = productMapper.selectById(product.getProdId());
        if (existProduct == null) {
            throw new BusinessException("Product does not exist");
        }

        // 2. Update product info
        product.setStockStatus(getStockStatus(product.getStock()));
        product.setUpdateDate(LocalDateTime.now());

        // 3. Persist to database
        int result = productMapper.updateById(product);
        if (result == 0) {
            throw new BusinessException("Failed to update product");
        }

        log.info("Product updated successfully, productId: {}", product.getProdId());

        // 4. Return updated product info
        return getProductById(product.getProdId());
    }

    @Override
    public boolean deleteProduct(Long prodId) {
        Product product = productMapper.selectById(prodId);
        if (product == null) {
            throw new BusinessException("Product does not exist");
        }

        // Logical delete: set status to DELETED
        product.setStatus(Constants.PRODUCT_STATUS_DELETED);
        product.setUpdateDate(LocalDateTime.now());

        int result = productMapper.updateById(product);
        log.info("Product deleted successfully, productId: {}", prodId);
        return result > 0;
    }

    @Override
    public Product getProductById(Long prodId) {
        Product product = productMapper.selectById(prodId);
        if (product == null) {
            throw new BusinessException("Product does not exist");
        }
        // Populate merchant name
        fillMerchantName(java.util.Collections.singletonList(product));
        // Populate supplier info
        fillSupplierInfo(java.util.Collections.singletonList(product));
        return product;
    }

    @Override
    public Page<Product> getProductPage(Long current, Long size, Long catId, Long merchId, String keyword) {
        // 1. Build query conditions
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Product::getStatus, Constants.PRODUCT_STATUS_DELETED); // Exclude deleted items
        
        if (catId != null) {
            wrapper.eq(Product::getCatId, catId);
        }
        if (merchId != null) {
            wrapper.eq(Product::getMerchId, merchId);
        }
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Product::getName, keyword)
                   .or()
                   .like(Product::getDescription, keyword);
        }
        
        // Order by creation time descending
        wrapper.orderByDesc(Product::getCreateDate);

        // 2. Paginate query
        Page<Product> page = new Page<>(current, size);
        Page<Product> resultPage = productMapper.selectPage(page, wrapper);
        fillMerchantName(resultPage.getRecords());
        fillSupplierInfo(resultPage.getRecords());
        return resultPage;
    }

    @Override
    public boolean updateProductStatus(Long prodId, String status) {
        Product product = productMapper.selectById(prodId);
        if (product == null) {
            throw new BusinessException("Product does not exist");
        }

        product.setStatus(status);
        product.setUpdateDate(LocalDateTime.now());

        int result = productMapper.updateById(product);
        log.info("Product status updated successfully, productId: {}, status: {}", prodId, status);
        return result > 0;
    }

    /**
     * Determine stock status based on inventory quantity.
     */
    private String getStockStatus(Integer stock) {
        if (stock == 0) {
            return Constants.STOCK_STATUS_OUT_OF_STOCK;
        } else if (stock < 10) {
            return Constants.STOCK_STATUS_LOW_STOCK;
        } else {
            return Constants.STOCK_STATUS_IN_STOCK;
        }
    }

    /**
     * Populate merchant names in batch to avoid extra frontend queries.
     */
    private void fillMerchantName(java.util.List<Product> products) {
        if (products == null || products.isEmpty()) {
            return;
        }
        java.util.Set<Long> merchIds = products.stream()
                .map(Product::getMerchId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());
        if (merchIds.isEmpty()) {
            return;
        }
        java.util.List<Merchant> merchants = merchantMapper.selectBatchIds(merchIds);
        java.util.Map<Long, String> merchNameMap = merchants.stream()
                .collect(java.util.stream.Collectors.toMap(Merchant::getMerchId, Merchant::getName));
        products.forEach(p -> p.setMerchantName(merchNameMap.get(p.getMerchId())));
    }

    /**
     * Populate supplier information in batch.
     */
    private void fillSupplierInfo(java.util.List<Product> products) {
        if (products == null || products.isEmpty()) {
            return;
        }
        java.util.Set<Long> supplierIds = products.stream()
                .map(Product::getSupplierId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());
        if (supplierIds.isEmpty()) {
            return;
        }
        java.util.List<Supplier> suppliers = supplierMapper.selectBatchIds(supplierIds);
        java.util.Map<Long, Supplier> supplierMap = suppliers.stream()
                .collect(java.util.stream.Collectors.toMap(Supplier::getId, s -> s));
        products.forEach(p -> {
            if (p.getSupplierId() != null) {
                Supplier s = supplierMap.get(p.getSupplierId());
                if (s != null) {
                    p.setSupplierName(s.getName());
                    p.setSupplierRating(s.getRating() != null ? s.getRating().doubleValue() : null);
                }
            }
        });
    }
}
