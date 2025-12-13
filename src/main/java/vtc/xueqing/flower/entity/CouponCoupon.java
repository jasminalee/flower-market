package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 优惠券券码实体类
 * 对应数据库表：coupon_coupons
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
     * 使用用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 是否已使用：0-未使用，1-已使用
     */
    @TableField("used")
    private Integer used;

    /**
     * 使用时间
     */
    @TableField("used_date")
    private LocalDateTime usedDate;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;
}
