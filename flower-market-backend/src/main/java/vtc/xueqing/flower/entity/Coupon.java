package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Coupon entity class
 * Corresponds to database table: coupons
 */
@Data
@TableName("coupons")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Coupon ID
     */
    @TableId(value = "coupon_id", type = IdType.AUTO)
    private Long couponId;

    /**
     * Merchant ID, NULL means platform coupon
     */
    @TableField("merch_id")
    private Long merchId;

    /**
     * Coupon name
     */
    @TableField("name")
    private String name;

    /**
     * Coupon type: DISCOUNT-discount coupon, FULL_REDUCTION-full reduction coupon, FIXED_AMOUNT-fixed amount coupon
     */
    @TableField("type")
    private String type;

    /**
     * Benefit value (discount percentage or amount)
     */
    @TableField("value")
    private BigDecimal value;

    /**
     * Minimum spending amount
     */
    @TableField("min_price")
    private BigDecimal minPrice;

    /**
     * Total issuance quantity
     */
    @TableField("total_quantity")
    private Integer totalQuantity;

    /**
     * Quantity claimed
     */
    @TableField("received_quantity")
    private Integer receivedQuantity;

    /**
     * Validity period start
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * Validity period end
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * Status: ACTIVE-active, INACTIVE-inactive, EXPIRED-expired
     */
    @TableField("status")
    private String status;

    /**
     * Coupon description
     */
    @TableField("description")
    private String description;

    /**
     * Creation time
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * Update time
     */
    @TableField("update_date")
    private LocalDateTime updateDate;
}
