package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.ProductFavorite;
import vtc.xueqing.flower.mapper.ProductFavoriteMapper;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.service.ProductFavoriteService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品收藏服务实现类
 */
@Service
public class ProductFavoriteServiceImpl implements ProductFavoriteService {
    
    @Resource
    private ProductFavoriteMapper productFavoriteMapper;
    
    @Resource
    private ProductMapper productMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductFavorite addFavorite(ProductFavorite productFavorite) {
        // 1. 检查商品是否存在
        Product product = productMapper.selectById(productFavorite.getProdId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        // 2. 检查是否已收藏
        LambdaQueryWrapper<ProductFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFavorite::getUserId, productFavorite.getUserId())
                .eq(ProductFavorite::getProdId, productFavorite.getProdId());
        Long count = productFavoriteMapper.selectCount(wrapper);
        
        if (count > 0) {
            throw new RuntimeException("已收藏该商品");
        }
        
        // 3. 添加收藏
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
            throw new RuntimeException("未找到收藏记录");
        }
    }
    
    @Override
    public List<ProductFavorite> getUserFavorites(Long userId) {
        LambdaQueryWrapper<ProductFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductFavorite::getUserId, userId)
                .orderByDesc(ProductFavorite::getFavDate);
        
        return productFavoriteMapper.selectList(wrapper);
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
