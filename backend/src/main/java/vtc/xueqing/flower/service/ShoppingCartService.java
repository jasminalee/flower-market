package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.ShoppingCart;
import vtc.xueqing.flower.vo.ShoppingCartVO;

import java.util.List;

/**
 * Shopping cart service interface.
 */
public interface ShoppingCartService {
    
    /**
     * Get user's shopping cart list (including product info).
     * @param userId user ID
     * @return shopping cart list
     */
    List<ShoppingCartVO> getCartByUserId(Long userId);
    
    /**
     * Add product to cart (increase quantity if exists).
     * @param shoppingCart cart info
     * @return cart item
     */
    ShoppingCart addToCart(ShoppingCart shoppingCart);
    
    /**
     * Update quantity of a cart item.
     * @param cartId cart ID
     * @param quantity new quantity
     * @return updated cart item
     */
    ShoppingCart updateCartQuantity(Long cartId, Integer quantity);
    
    /**
     * Delete a cart item.
     * @param cartId cart ID
     */
    void deleteCartItem(Long cartId);
    
    /**
     * Clear a user's cart.
     * @param userId user ID
     */
    void clearCart(Long userId);
    
    /**
     * Batch delete cart items.
     * @param cartIds cart ID list
     */
    void batchDeleteCartItems(List<Long> cartIds);
}
