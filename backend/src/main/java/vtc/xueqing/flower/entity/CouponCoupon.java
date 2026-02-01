package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Coupon Code Entity Class
 * Corresponds to database table: coupon_coupons
 */
@Data
@TableName("coupon_coupons")
public class CouponCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Coupon ID
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * Coupon Code
     */
    @TableField("code")
    private String code;

    /**
     * User ID Who Used
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Whether Used: 0-Not Used, 1-Used
     */
    @TableField("used")
    private Integer used;

    /**
     * Usage Time
     */
    @TableField("used_date")
    private LocalDateTime usedDate;

    /**
     * Creation Time
     */
    @TableField("create_date")
    private LocalDateTime createDate;
}
