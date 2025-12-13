package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.ProductFavorite;

import java.util.List;

/**
 * 产品收藏服务接口
 */
public interface ProductFavoriteService {
    
    /**
     * 添加收藏
     * @param productFavorite 收藏信息
     * @return 收藏记录
     */
    ProductFavorite addFavorite(ProductFavorite productFavorite);
    
    /**
     * 取消收藏
     * @param userId 用户ID
     * @param prodId 产品ID
     */
    void removeFavorite(Long userId, Long prodId);
    
    /**
     * 获取用户的收藏列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<ProductFavorite> getUserFavorites(Long userId);
    
    /**
     * 检查用户是否已收藏某商品
     * @param userId 用户ID
     * @param prodId 产品ID
     * @return 是否已收藏
     */
    boolean isFavorited(Long userId, Long prodId);
}
