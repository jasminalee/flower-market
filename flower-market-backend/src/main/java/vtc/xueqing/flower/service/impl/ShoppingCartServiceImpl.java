package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.ShoppingCart;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.mapper.ShoppingCartMapper;
import vtc.xueqing.flower.service.ShoppingCartService;
import vtc.xueqing.flower.vo.ShoppingCartVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * Shopping cart service implementation.
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    
    @Resource
    private ProductMapper productMapper;
    
    @Override
    public List<ShoppingCartVO> getCartByUserId(Long userId) {
        return shoppingCartMapper.selectCartWithProductByUserId(userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShoppingCart addToCart(ShoppingCart shoppingCart) {
        // 1. Check whether the product exists
        Product product = productMapper.selectById(shoppingCart.getProdId());
        if (product == null) {
            throw new RuntimeException("Product does not exist");
        }
        
        // 2. Check product status
        if (!"ACTIVE".equals(product.getStatus())) {
            throw new RuntimeException("Product is unavailable");
        }
        
        // 3. Check inventory
        if (product.getStock() < shoppingCart.getQuantity()) {
            throw new RuntimeException("Insufficient product stock");
        }
        
        // 4. Check whether the product already exists in the cart
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(ShoppingCart::getProdId, shoppingCart.getProdId());
        ShoppingCart existingCart = shoppingCartMapper.selectOne(wrapper);
        
        if (existingCart != null) {
            // If present, increase quantity
            int newQuantity = existingCart.getQuantity() + shoppingCart.getQuantity();
            
            // Ensure the new quantity does not exceed stock
            if (newQuantity > product.getStock()) {
                throw new RuntimeException("Cart quantity exceeds available stock");
            }
            
            existingCart.setQuantity(newQuantity);
            shoppingCartMapper.updateById(existingCart);
            return existingCart;
        } else {
            // Otherwise, add a new record
            if (shoppingCart.getSelected() == null) {
                shoppingCart.setSelected(1); // Default to selected
            }
            shoppingCartMapper.insert(shoppingCart);
            return shoppingCart;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShoppingCart updateCartQuantity(Long cartId, Integer quantity) {
        // 1. Get cart item
        ShoppingCart cart = shoppingCartMapper.selectById(cartId);
        if (cart == null) {
            throw new RuntimeException("Cart item does not exist");
        }
        
        // 2. Check product stock
        Product product = productMapper.selectById(cart.getProdId());
        if (product == null) {
            throw new RuntimeException("Product does not exist");
        }
        
        if (quantity > product.getStock()) {
            throw new RuntimeException("Insufficient stock; current inventory: " + product.getStock());
        }
        
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        
        // 3. Update quantity
        cart.setQuantity(quantity);
        shoppingCartMapper.updateById(cart);
        
        return cart;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(Long cartId) {
        ShoppingCart cart = shoppingCartMapper.selectById(cartId);
        if (cart == null) {
            throw new RuntimeException("Cart item does not exist");
        }
        shoppingCartMapper.deleteById(cartId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartMapper.delete(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCartItems(List<Long> cartIds) {
        if (cartIds == null || cartIds.isEmpty()) {
            throw new RuntimeException("Cart ID list cannot be empty");
        }
        shoppingCartMapper.deleteBatchIds(cartIds);
    }
}
