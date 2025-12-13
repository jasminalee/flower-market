package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体类
 * 对应数据库表：customer_coupons
 */
@Data
@TableName("customer_coupons")
public class CustomerCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 优惠券ID
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * 券码
     */
    @TableField("code")
    private String code;

    /**
     * 状态：UNUSED-未使用，USED-已使用，EXPIRED-已过期
     */
    @TableField("status")
    private String status;

    /**
     * 领取时间
     */
    @TableField("receive_date")
    private LocalDateTime receiveDate;

    /**
     * 使用时间
     */
    @TableField("used_date")
    private LocalDateTime usedDate;

    /**
     * 使用的订单ID
     */
    @TableField("order_id")
    private Long orderId;
}
