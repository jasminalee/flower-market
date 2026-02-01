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
 * Shopping Cart Controller
 */
@Api(tags = "Shopping Cart Management")
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {
    
    @Resource
    private ShoppingCartService shoppingCartService;
    
    @ApiOperation("Get User Shopping Cart List")
    @GetMapping
    public Result<List<ShoppingCartVO>> getCart(@ApiParam("User ID") @RequestParam Long userId) {
        try {
            List<ShoppingCartVO> cartList = shoppingCartService.getCartByUserId(userId);
            return Result.success(cartList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Add Item to Shopping Cart")
    @PostMapping("/items")
    public Result<ShoppingCart> addToCart(@RequestBody ShoppingCart shoppingCart) {
        try {
            ShoppingCart cart = shoppingCartService.addToCart(shoppingCart);
            return Result.success(cart);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update Shopping Cart Item Quantity")
    @PutMapping("/items/{id}")
    public Result<ShoppingCart> updateCartQuantity(
            @PathVariable("id") Long id,
            @ApiParam("New Quantity") @RequestParam Integer quantity
    ) {
        try {
            ShoppingCart cart = shoppingCartService.updateCartQuantity(id, quantity);
            return Result.success(cart);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Delete Shopping Cart Item")
    @DeleteMapping("/items/{id}")
    public Result<Void> deleteCartItem(@PathVariable("id") Long id) {
        try {
            shoppingCartService.deleteCartItem(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Clear Shopping Cart")
    @DeleteMapping
    public Result<Void> clearCart(@ApiParam("User ID") @RequestParam Long userId) {
        try {
            shoppingCartService.clearCart(userId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Batch Delete Shopping Cart Items")
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
