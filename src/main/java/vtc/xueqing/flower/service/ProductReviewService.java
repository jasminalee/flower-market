package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.ProductReview;

/**
 * 产品评价服务接口
 */
public interface ProductReviewService {
    
    /**
     * 发布评价
     * @param productReview 评价信息
     * @return 评价记录
     */
    ProductReview addReview(ProductReview productReview);
    
    /**
     * 获取商品的评价列表（分页）
     * @param page 分页信息
     * @param prodId 产品ID
     * @param rating 评分（可选）
     * @return 评价列表
     */
    IPage<ProductReview> getProductReviews(Page<ProductReview> page, Long prodId, Integer rating);
    
    /**
     * 审核评价（管理员操作）
     * @param reviewId 评价ID
     * @param status 审核状态：APPROVED-已通过，REJECTED-已拒绝
     * @return 更新后的评价
     */
    ProductReview reviewApproval(Long reviewId, String status);
}
