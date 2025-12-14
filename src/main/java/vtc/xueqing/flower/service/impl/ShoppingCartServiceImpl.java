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
 * 购物车服务实现类
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
        // 1. 检查商品是否存在
        Product product = productMapper.selectById(shoppingCart.getProdId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        // 2. 检查商品状态
        if (!"ACTIVE".equals(product.getStatus())) {
            throw new RuntimeException("商品已下架");
        }
        
        // 3. 检查库存
        if (product.getStock() < shoppingCart.getQuantity()) {
            throw new RuntimeException("商品库存不足");
        }
        
        // 4. 检查购物车中是否已存在该商品
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                .eq(ShoppingCart::getProdId, shoppingCart.getProdId());
        ShoppingCart existingCart = shoppingCartMapper.selectOne(wrapper);
        
        if (existingCart != null) {
            // 如果已存在，增加数量
            int newQuantity = existingCart.getQuantity() + shoppingCart.getQuantity();
            
            // 检查新数量是否超过库存
            if (newQuantity > product.getStock()) {
                throw new RuntimeException("购物车商品数量超过库存");
            }
            
            existingCart.setQuantity(newQuantity);
            shoppingCartMapper.updateById(existingCart);
            return existingCart;
        } else {
            // 如果不存在，添加新记录
            if (shoppingCart.getSelected() == null) {
                shoppingCart.setSelected(1); // 默认选中
            }
            shoppingCartMapper.insert(shoppingCart);
            return shoppingCart;
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShoppingCart updateCartQuantity(Long cartId, Integer quantity) {
        // 1. 获取购物车项
        ShoppingCart cart = shoppingCartMapper.selectById(cartId);
        if (cart == null) {
            throw new RuntimeException("购物车项不存在");
        }
        
        // 2. 检查商品库存
        Product product = productMapper.selectById(cart.getProdId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        if (quantity > product.getStock()) {
            throw new RuntimeException("商品库存不足，当前库存：" + product.getStock());
        }
        
        if (quantity <= 0) {
            throw new RuntimeException("数量必须大于0");
        }
        
        // 3. 更新数量
        cart.setQuantity(quantity);
        shoppingCartMapper.updateById(cart);
        
        return cart;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(Long cartId) {
        ShoppingCart cart = shoppingCartMapper.selectById(cartId);
        if (cart == null) {
            throw new RuntimeException("购物车项不存在");
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
            throw new RuntimeException("购物车ID列表不能为空");
        }
        shoppingCartMapper.deleteBatchIds(cartIds);
    }
}
