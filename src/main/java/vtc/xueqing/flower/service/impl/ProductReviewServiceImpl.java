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
 * 产品评价服务实现类
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
        // 1. 检查商品是否存在
        Product product = productMapper.selectById(productReview.getProdId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        // 2. 检查评分范围
        if (productReview.getRating() < 1 || productReview.getRating() > 5) {
            throw new RuntimeException("评分必须在1-5星之间");
        }
        
        // 3. 如果提供了订单ID，检查订单是否存在且已完成
        if (productReview.getOrderId() != null) {
            Order order = orderMapper.selectById(productReview.getOrderId());
            if (order == null) {
                throw new RuntimeException("订单不存在");
            }
            
            if ("COMPLETED".equals(order.getStatus())) {
                productReview.setVerified(1); // 已购买验证
            }
        } else {
            productReview.setVerified(0); // 未验证
        }
        
        // 4. 设置初始状态为待审核
        if (productReview.getStatus() == null || productReview.getStatus().isEmpty()) {
            productReview.setStatus("PENDING");
        }
        
        // 5. 保存评价
        productReviewMapper.insert(productReview);
        
        return productReview;
    }
    
    @Override
    public IPage<ProductReview> getProductReviews(Page<ProductReview> page, Long prodId, Integer rating) {
        LambdaQueryWrapper<ProductReview> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(ProductReview::getProdId, prodId)
                .eq(ProductReview::getStatus, "APPROVED") // 只显示已审核通过的评价
                .eq(rating != null, ProductReview::getRating, rating)
                .orderByDesc(ProductReview::getCreateDate);
        
        return productReviewMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductReview reviewApproval(Long reviewId, String status) {
        // 1. 获取评价
        ProductReview review = productReviewMapper.selectById(reviewId);
        if (review == null) {
            throw new RuntimeException("评价不存在");
        }
        
        // 2. 检查状态是否合法
        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new RuntimeException("状态只能是APPROVED或REJECTED");
        }
        
        // 3. 更新状态
        review.setStatus(status);
        productReviewMapper.updateById(review);
        
        return review;
    }
}
