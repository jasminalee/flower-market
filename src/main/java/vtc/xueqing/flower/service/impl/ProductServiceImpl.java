package vtc.xueqing.flower.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.exception.BusinessException;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.service.ProductService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 产品服务实现类
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public Product createProduct(Product product) {
        // 1. 设置默认值
        product.setSales(0);
        product.setStatus(Constants.PRODUCT_STATUS_ACTIVE);
        product.setStockStatus(getStockStatus(product.getStock()));
        product.setCreateDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());

        // 2. 保存到数据库
        int result = productMapper.insert(product);
        if (result == 0) {
            throw new BusinessException("创建产品失败");
        }

        log.info("创建产品成功，产品ID：{}, 名称：{}", product.getProdId(), product.getName());
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        if (product.getProdId() == null) {
            throw new BusinessException("产品ID不能为空");
        }

        // 1. 查询产品是否存在
        Product existProduct = productMapper.selectById(product.getProdId());
        if (existProduct == null) {
            throw new BusinessException("产品不存在");
        }

        // 2. 更新产品信息
        product.setStockStatus(getStockStatus(product.getStock()));
        product.setUpdateDate(LocalDateTime.now());

        // 3. 保存到数据库
        int result = productMapper.updateById(product);
        if (result == 0) {
            throw new BusinessException("更新产品失败");
        }

        log.info("更新产品成功，产品ID：{}", product.getProdId());

        // 4. 返回更新后的产品信息
        return getProductById(product.getProdId());
    }

    @Override
    public boolean deleteProduct(Long prodId) {
        Product product = productMapper.selectById(prodId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        // 逻辑删除：将状态设置为DELETED
        product.setStatus(Constants.PRODUCT_STATUS_DELETED);
        product.setUpdateDate(LocalDateTime.now());

        int result = productMapper.updateById(product);
        log.info("删除产品成功，产品ID：{}", prodId);
        return result > 0;
    }

    @Override
    public Product getProductById(Long prodId) {
        Product product = productMapper.selectById(prodId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }
        return product;
    }

    @Override
    public Page<Product> getProductPage(Long current, Long size, Long catId, Long merchId, String keyword) {
        // 1. 构建查询条件
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Product::getStatus, Constants.PRODUCT_STATUS_DELETED); // 排除已删除的
        
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
        
        // 按创建时间倒序
        wrapper.orderByDesc(Product::getCreateDate);

        // 2. 分页查询
        Page<Product> page = new Page<>(current, size);
        return productMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean updateProductStatus(Long prodId, String status) {
        Product product = productMapper.selectById(prodId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        product.setStatus(status);
        product.setUpdateDate(LocalDateTime.now());

        int result = productMapper.updateById(product);
        log.info("更新产品状态成功，产品ID：{}, 状态：{}", prodId, status);
        return result > 0;
    }

    /**
     * 根据库存数量判断库存状态
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
}
