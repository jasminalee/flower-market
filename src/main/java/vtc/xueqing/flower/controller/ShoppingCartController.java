package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.ShoppingCart;
import vtc.xueqing.flower.service.ShoppingCartService;
import vtc.xueqing.flower.vo.ShoppingCartVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * 购物车控制器
 */
@Api(tags = "购物车管理")
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    
    @Resource
    private ShoppingCartService shoppingCartService;
    
    @ApiOperation("获取用户购物车列表")
    @GetMapping
    public Result<List<ShoppingCartVO>> getCart(@ApiParam("用户ID") @RequestParam Long userId) {
        try {
            List<ShoppingCartVO> cartList = shoppingCartService.getCartByUserId(userId);
            return Result.success(cartList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("添加商品到购物车")
    @PostMapping("/items")
    public Result<ShoppingCart> addToCart(@RequestBody ShoppingCart shoppingCart) {
        try {
            ShoppingCart cart = shoppingCartService.addToCart(shoppingCart);
            return Result.success(cart);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新购物车商品数量")
    @PutMapping("/items/{id}")
    public Result<ShoppingCart> updateCartQuantity(
            @PathVariable("id") Long id,
            @ApiParam("新数量") @RequestParam Integer quantity
    ) {
        try {
            ShoppingCart cart = shoppingCartService.updateCartQuantity(id, quantity);
            return Result.success(cart);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除购物车商品")
    @DeleteMapping("/items/{id}")
    public Result<Void> deleteCartItem(@PathVariable("id") Long id) {
        try {
            shoppingCartService.deleteCartItem(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("清空购物车")
    @DeleteMapping
    public Result<Void> clearCart(@ApiParam("用户ID") @RequestParam Long userId) {
        try {
            shoppingCartService.clearCart(userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("批量删除购物车商品")
    @DeleteMapping("/items/batch")
    public Result<Void> batchDeleteCartItems(@RequestBody List<Long> cartIds) {
        try {
            shoppingCartService.batchDeleteCartItems(cartIds);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
