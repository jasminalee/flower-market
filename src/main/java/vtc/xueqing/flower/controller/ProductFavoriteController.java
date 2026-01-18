package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.ProductFavorite;
import vtc.xueqing.flower.service.ProductFavoriteService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Product Favorite Controller
 */
@Api(tags = "Product Favorite Management")
@RestController
@RequestMapping("/api/products")
public class ProductFavoriteController {
    
    @Resource
    private ProductFavoriteService productFavoriteService;
    
    @ApiOperation("Add Favorite")
    @PostMapping("/{id}/favorite")
    public Result<ProductFavorite> addFavorite(
            @PathVariable("id") Long prodId,
            @ApiParam("User ID") @RequestParam Long userId
    ) {
        try {
            ProductFavorite productFavorite = new ProductFavorite();
            productFavorite.setUserId(userId);
            productFavorite.setProdId(prodId);
            
            ProductFavorite favorite = productFavoriteService.addFavorite(productFavorite);
            return Result.success(favorite);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Remove Favorite")
    @DeleteMapping("/{id}/favorite")
    public Result<Void> removeFavorite(
            @PathVariable("id") Long prodId,
            @ApiParam("User ID") @RequestParam Long userId
    ) {
        try {
            productFavoriteService.removeFavorite(userId, prodId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get User Favorites List")
    @GetMapping("/favorites")
    public Result<List<vtc.xueqing.flower.entity.Product>> getUserFavorites(
            @ApiParam("User ID") @RequestParam Long userId
    ) {
        try {
            List<vtc.xueqing.flower.entity.Product> favorites = productFavoriteService.getUserFavorites(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Check if Favorited")
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> checkFavorite(
            @PathVariable("id") Long prodId,
            @ApiParam("User ID") @RequestParam Long userId
    ) {
        try {
            boolean isFavorited = productFavoriteService.isFavorited(userId, prodId);
            return Result.success(isFavorited);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
