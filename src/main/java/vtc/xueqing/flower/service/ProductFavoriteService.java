package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.ProductFavorite;

import java.util.List;

/**
 * Product favorite service interface.
 */
public interface ProductFavoriteService {
    
    /**
     * Add favorite.
     * @param productFavorite favorite info
     * @return favorite record
     */
    ProductFavorite addFavorite(ProductFavorite productFavorite);
    
    /**
     * Remove favorite.
     * @param userId user ID
     * @param prodId product ID
     */
    void removeFavorite(Long userId, Long prodId);
    
    /**
     * Get user's favorited products (with merchant info).
     * @param userId user ID
     * @return favorite product list
     */
    List<Product> getUserFavorites(Long userId);
    
    /**
     * Check if user has favorited a product.
     * @param userId user ID
     * @param prodId product ID
     * @return whether favorited
     */
    boolean isFavorited(Long userId, Long prodId);
}
