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
 * Product Review Controller
 */
@Api(tags = "Product Review Management")
@RestController
@RequestMapping("/api/products")
public class ProductReviewController {
    
    @Resource
    private ProductReviewService productReviewService;
    
    @ApiOperation("Submit Review")
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
    
    @ApiOperation("Get Product Reviews List (Pagination)")
    @GetMapping("/{id}/reviews")
    public Result<IPage<ProductReview>> getProductReviews(
            @PathVariable("id") Long prodId,
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Rating Filter") @RequestParam(required = false) Integer rating
    ) {
        try {
            Page<ProductReview> page = new Page<>(current, size);
            IPage<ProductReview> reviewPage = productReviewService.getProductReviews(page, prodId, rating);
            return Result.success(reviewPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Review Approval (Admin)")
    @PutMapping("/reviews/{id}/approval")
    public Result<ProductReview> reviewApproval(
            @PathVariable("id") Long reviewId,
            @ApiParam("Review Status: APPROVED-approved, REJECTED-rejected") @RequestParam String status
    ) {
        try {
            ProductReview review = productReviewService.reviewApproval(reviewId, status);
            return Result.success(review);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
