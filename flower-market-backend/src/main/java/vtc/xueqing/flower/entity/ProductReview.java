package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product review entity class
 * Corresponds to database table: product_reviews
 */
@Data
@TableName("product_reviews")
public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Review ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Product ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * Order item ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * Rating (1-5)
     */
    @TableField("rating")
    private Integer rating;

    /**
     * Review content
     */
    @TableField("content")
    private String content;

    /**
     * Review images (JSON array)
     */
    @TableField("images")
    private String images;

    /**
     * Purchase verification: 0-not verified, 1-verified
     */
    @TableField("verified")
    private Integer verified;

    /**
     * Status: PENDING-pending, APPROVED-approved, REJECTED-rejected
     */
    @TableField("status")
    private String status;

    /**
     * Creation time
     */
    @TableField("create_date")
    private LocalDateTime createDate;
}
