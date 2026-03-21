package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.ProductReview;
import vtc.xueqing.flower.vo.ProductReviewVO;

/**
 * Product review service interface.
 */
public interface ProductReviewService {
    
    /**
     * Publish a review.
     * @param productReview review info
     * @return review record
     */
    ProductReview addReview(ProductReview productReview);
    
    /**
     * Get product review list (paginated).
     * @param page pagination info
     * @param prodId product ID
     * @param rating rating filter (optional)
     * @return review list
     */
    IPage<ProductReview> getProductReviews(Page<ProductReview> page, Long prodId, Integer rating);
    
    /**
     * Review moderation (admin).
     * @param reviewId review ID
     * @param status review status: APPROVED-approved, REJECTED-rejected
     * @return updated review
     */
    ProductReview reviewApproval(Long reviewId, String status);
    
    /**
     * Get all reviews (admin).
     * @param page pagination info
     * @param status status filter (optional): PENDING, APPROVED, REJECTED
     * @return review list
     */
    IPage<ProductReview> getAllReviews(Page<ProductReview> page, String status);
    
    /**
     * Get all reviews with detail (admin).
     * @param page pagination info
     * @param status status filter (optional): PENDING, APPROVED, REJECTED
     * @param rating rating filter (optional): 1-5
     * @return review VO list
     */
    IPage<ProductReviewVO> getAllReviewsWithDetail(Page<ProductReviewVO> page, String status, Integer rating);
}
