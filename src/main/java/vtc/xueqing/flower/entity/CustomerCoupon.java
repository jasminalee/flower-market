package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Customer coupon entity class
 * Corresponds to database table: customer_coupons
 */
@Data
@TableName("customer_coupons")
public class CustomerCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer coupon ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Coupon ID
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * Code
     */
    @TableField("code")
    private String code;

    /**
     * Status: UNUSED-unused, USED-used, EXPIRED-expired
     */
    @TableField("status")
    private String status;

    /**
     * Receipt time
     */
    @TableField("receive_date")
    private LocalDateTime receiveDate;

    /**
     * Usage time
     */
    @TableField("used_date")
    private LocalDateTime usedDate;

    /**
     * Used order ID
     */
    @TableField("order_id")
    private Long orderId;
}
