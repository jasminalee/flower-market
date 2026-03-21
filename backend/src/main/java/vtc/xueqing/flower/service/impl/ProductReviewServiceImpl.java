package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.ProductReview;
import vtc.xueqing.flower.mapper.OrderMapper;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.mapper.ProductReviewMapper;
import vtc.xueqing.flower.service.ProductReviewService;

import javax.annotation.Resource;

/**
 * Product review service implementation.
 */
@Service
public class ProductReviewServiceImpl implements ProductReviewService {
    
    @Resource
    private ProductReviewMapper productReviewMapper;
    
    @Resource
    private ProductMapper productMapper;
    
    @Resource
    private OrderMapper orderMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductReview addReview(ProductReview productReview) {
        // 1. Check whether the product exists
        Product product = productMapper.selectById(productReview.getProdId());
        if (product == null) {
            throw new RuntimeException("Product does not exist");
        }
        
        // 2. Validate rating range
        if (productReview.getRating() < 1 || productReview.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5 stars");
        }
        
        // 3. If an orderId is provided, ensure the order exists and is completed
        if (productReview.getOrderId() != null) {
            Order order = orderMapper.selectById(productReview.getOrderId());
            if (order == null) {
                throw new RuntimeException("Order does not exist");
            }
            
            if ("COMPLETED".equals(order.getStatus())) {
                productReview.setVerified(1); // Verified purchase
            }
        } else {
            productReview.setVerified(0); // Not verified
        }
        
        // 4. Initialize status to pending review
        if (productReview.getStatus() == null || productReview.getStatus().isEmpty()) {
            productReview.setStatus("PENDING");
        }
        
        // 5. Save review
        productReviewMapper.insert(productReview);
        
        return productReview;
    }
    
    @Override
    public IPage<ProductReview> getProductReviews(Page<ProductReview> page, Long prodId, Integer rating) {
        LambdaQueryWrapper<ProductReview> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(ProductReview::getProdId, prodId)
            .eq(ProductReview::getStatus, "APPROVED") // Only show approved reviews
                .eq(rating != null, ProductReview::getRating, rating)
                .orderByDesc(ProductReview::getCreateDate);
        
        return productReviewMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductReview reviewApproval(Long reviewId, String status) {
        // 1. Fetch review
        ProductReview review = productReviewMapper.selectById(reviewId);
        if (review == null) {
            throw new RuntimeException("Review does not exist");
        }
        
        // 2. Validate status value
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new RuntimeException("Status must be APPROVED or REJECTED");
        }
        
        // 3. Update status
        review.setStatus(status);
        productReviewMapper.updateById(review);
        
        return review;
    }
    
    @Override
    public IPage<ProductReview> getAllReviews(Page<ProductReview> page, String status) {
        LambdaQueryWrapper<ProductReview> wrapper = new LambdaQueryWrapper<>();
        
        // Filter by status when specified
        wrapper.eq(status != null && !status.isEmpty(), ProductReview::getStatus, status)
                .orderByDesc(ProductReview::getCreateDate);
        
        return productReviewMapper.selectPage(page, wrapper);
    }
    
    @Override
    public IPage<vtc.xueqing.flower.vo.ProductReviewVO> getAllReviewsWithDetail(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.vo.ProductReviewVO> page, 
            String status, Integer rating) {
        return productReviewMapper.selectAllReviewsWithDetail(page, status, rating);
    }
}
