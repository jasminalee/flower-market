package vtc.xueqing.flower.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券视图对象
 * 包含优惠券详细信息
 */
@Data
public class CustomerCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户优惠券ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 券码
     */
    private String code;

    /**
     * 状态：UNUSED-未使用，USED-已使用，EXPIRED-已过期
     */
    private String status;

    /**
     * 领取时间
     */
    private LocalDateTime receiveDate;

    /**
     * 使用时间
     */
    private LocalDateTime usedDate;

    /**
     * 使用的订单ID
     */
    private Long orderId;

    // ===== 优惠券详细信息 =====
    
    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券类型
     */
    private String type;

    /**
     * 优惠值
     */
    private BigDecimal value;

    /**
     * 最低消费金额
     */
    private BigDecimal minPrice;

    /**
     * 有效期开始
     */
    private LocalDateTime startDate;

    /**
     * 有效期结束
     */
    private LocalDateTime endDate;

    /**
     * 优惠券描述
     */
    private String description;
}
