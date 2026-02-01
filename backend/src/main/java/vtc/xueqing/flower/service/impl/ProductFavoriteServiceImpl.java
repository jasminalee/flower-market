package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.ProductFavorite;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.mapper.ProductFavoriteMapper;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.mapper.MerchantMapper;
import vtc.xueqing.flower.service.ProductFavoriteService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Product favorite service implementation.
 */
@Service
public class ProductFavoriteServiceImpl implements ProductFavoriteService {
    
    @Resource
    private ProductFavoriteMapper productFavoriteMapper;
    
    @Resource
    private ProductMapper productMapper;

    @Resource
    private MerchantMapper merchantMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductFavorite addFavorite(ProductFavorite productFavorite) {
        // 1. Check whether the product exists
        Product product = productMapper.selectById(productFavorite.getProdId());
        if (product == null) {
            throw new RuntimeException("Product does not exist");
        }
        
        // 2. Check whether it is already favorited
        LambdaQueryWrapper<ProductFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFavorite::getUserId, productFavorite.getUserId())
                .eq(ProductFavorite::getProdId, productFavorite.getProdId());
        Long count = productFavoriteMapper.selectCount(wrapper);
        
        if (count > 0) {
            throw new RuntimeException("Product already favorited");
        }
        
        // 3. Add favorite record
        productFavoriteMapper.insert(productFavorite);
        return productFavorite;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFavorite(Long userId, Long prodId) {
        LambdaQueryWrapper<ProductFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFavorite::getUserId, userId)
                .eq(ProductFavorite::getProdId, prodId);
        
        int deleted = productFavoriteMapper.delete(wrapper);
        if (deleted == 0) {
            throw new RuntimeException("Favorite record not found");
        }
    }
    
    @Override
        public List<Product> getUserFavorites(Long userId) {
        LambdaQueryWrapper<ProductFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFavorite::getUserId, userId)
            .orderByDesc(ProductFavorite::getFavDate);
        List<ProductFavorite> favorites = productFavoriteMapper.selectList(wrapper);

        if (favorites.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        java.util.Set<Long> prodIds = favorites.stream()
            .map(ProductFavorite::getProdId)
            .collect(java.util.stream.Collectors.toSet());
        List<Product> products = productMapper.selectBatchIds(prodIds);

        // Populate merchant names
        java.util.Set<Long> merchIds = products.stream()
            .map(Product::getMerchId)
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toSet());
        java.util.Map<Long, String> merchMap = merchantMapper.selectBatchIds(merchIds).stream()
            .collect(java.util.stream.Collectors.toMap(Merchant::getMerchId, Merchant::getName));
        products.forEach(p -> p.setMerchantName(merchMap.get(p.getMerchId())));

        // Keep ordering consistent with favorites
        java.util.Map<Long, Product> productMap = products.stream()
            .collect(java.util.stream.Collectors.toMap(Product::getProdId, p -> p));
        return favorites.stream()
            .map(fav -> productMap.get(fav.getProdId()))
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public boolean isFavorited(Long userId, Long prodId) {
        LambdaQueryWrapper<ProductFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFavorite::getUserId, userId)
                .eq(ProductFavorite::getProdId, prodId);
        
        Long count = productFavoriteMapper.selectCount(wrapper);
        return count > 0;
    }
}
