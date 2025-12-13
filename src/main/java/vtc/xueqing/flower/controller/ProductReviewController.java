package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.ProductReview;
import vtc.xueqing.flower.service.ProductReviewService;

import javax.annotation.Resource;

/**
 * 产品评价控制器
 */
@Api(tags = "产品评价管理")
@RestController
@RequestMapping("/api/products")
public class ProductReviewController {
    
    @Resource
    private ProductReviewService productReviewService;
    
    @ApiOperation("发布评价")
    @PostMapping("/{id}/review")
    public Result<ProductReview> addReview(
            @PathVariable("id") Long prodId,
            @RequestBody ProductReview productReview
    ) {
        try {
            productReview.setProdId(prodId);
            ProductReview review = productReviewService.addReview(productReview);
            return Result.success(review);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商品评价列表（分页）")
    @GetMapping("/{id}/reviews")
    public Result<IPage<ProductReview>> getProductReviews(
            @PathVariable("id") Long prodId,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("评分筛选") @RequestParam(required = false) Integer rating
    ) {
        try {
            Page<ProductReview> page = new Page<>(current, size);
            IPage<ProductReview> reviewPage = productReviewService.getProductReviews(page, prodId, rating);
            return Result.success(reviewPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("审核评价（管理员）")
    @PutMapping("/reviews/{id}/approval")
    public Result<ProductReview> reviewApproval(
            @PathVariable("id") Long reviewId,
            @ApiParam("审核状态：APPROVED-已通过，REJECTED-已拒绝") @RequestParam String status
    ) {
        try {
            ProductReview review = productReviewService.reviewApproval(reviewId, status);
            return Result.success(review);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
