package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.ShoppingCart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface ShoppingCartService {
    
    /**
     * 获取用户的购物车列表
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<ShoppingCart> getCartByUserId(Long userId);
    
    /**
     * 添加商品到购物车（如果已存在则增加数量）
     * @param shoppingCart 购物车信息
     * @return 购物车项
     */
    ShoppingCart addToCart(ShoppingCart shoppingCart);
    
    /**
     * 更新购物车商品数量
     * @param cartId 购物车ID
     * @param quantity 新数量
     * @return 更新后的购物车项
     */
    ShoppingCart updateCartQuantity(Long cartId, Integer quantity);
    
    /**
     * 删除购物车商品
     * @param cartId 购物车ID
     */
    void deleteCartItem(Long cartId);
    
    /**
     * 清空用户购物车
     * @param userId 用户ID
     */
    void clearCart(Long userId);
    
    /**
     * 批量删除购物车商品
     * @param cartIds 购物车ID列表
     */
    void batchDeleteCartItems(List<Long> cartIds);
}
