package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品评价实体类
 * 对应数据库表：product_reviews
 */
@Data
@TableName("product_reviews")
public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 产品ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 评分：1-5星
     */
    @TableField("rating")
    private Integer rating;

    /**
     * 评价内容
     */
    @TableField("content")
    private String content;

    /**
     * 评价图片（JSON数组）
     */
    @TableField("images")
    private String images;

    /**
     * 是否已购买验证：0-未验证，1-已验证
     */
    @TableField("verified")
    private Integer verified;

    /**
     * 状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;
}
