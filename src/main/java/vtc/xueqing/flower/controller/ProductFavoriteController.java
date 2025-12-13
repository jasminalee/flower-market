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
 * 产品收藏控制器
 */
@Api(tags = "产品收藏管理")
@RestController
@RequestMapping("/api/products")
public class ProductFavoriteController {
    
    @Resource
    private ProductFavoriteService productFavoriteService;
    
    @ApiOperation("添加收藏")
    @PostMapping("/{id}/favorite")
    public Result<ProductFavorite> addFavorite(
            @PathVariable("id") Long prodId,
            @ApiParam("用户ID") @RequestParam Long userId
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
    
    @ApiOperation("取消收藏")
    @DeleteMapping("/{id}/favorite")
    public Result<Void> removeFavorite(
            @PathVariable("id") Long prodId,
            @ApiParam("用户ID") @RequestParam Long userId
    ) {
        try {
            productFavoriteService.removeFavorite(userId, prodId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取用户收藏列表")
    @GetMapping("/favorites")
    public Result<List<ProductFavorite>> getUserFavorites(
            @ApiParam("用户ID") @RequestParam Long userId
    ) {
        try {
            List<ProductFavorite> favorites = productFavoriteService.getUserFavorites(userId);
            return Result.success(favorites);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("检查是否已收藏")
    @GetMapping("/{id}/favorite/check")
    public Result<Boolean> checkFavorite(
            @PathVariable("id") Long prodId,
            @ApiParam("用户ID") @RequestParam Long userId
    ) {
        try {
            boolean isFavorited = productFavoriteService.isFavorited(userId, prodId);
            return Result.success(isFavorited);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
