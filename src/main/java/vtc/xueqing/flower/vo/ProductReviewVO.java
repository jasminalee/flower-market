package vtc.xueqing.flower.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品评价VO - 用于管理员查看评价列表
 * 包含关联的用户和产品信息
 */
@Data
public class ProductReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品图片
     */
    private String productImage;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 评分：1-5星
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片（JSON数组）
     */
    private String images;

    /**
     * 是否已购买验证：0-未验证，1-已验证
     */
    private Integer verified;

    /**
     * 状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;
}
