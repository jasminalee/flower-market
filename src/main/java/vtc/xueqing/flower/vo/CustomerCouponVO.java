package vtc.xueqing.flower.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Customer coupon view object with coupon details.
 */
@Data
public class CustomerCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Customer coupon ID.
     */
    private Long id;

    /**
     * User ID.
     */
    private Long userId;

    /**
     * Coupon ID.
     */
    private Long couponId;

    /**
     * Coupon code.
     */
    private String code;

    /**
     * Status: UNUSED-unused, USED-used, EXPIRED-expired.
     */
    private String status;

    /**
     * Received time.
     */
    private LocalDateTime receiveDate;

    /**
     * Used time.
     */
    private LocalDateTime usedDate;

    /**
     * Order ID where used.
     */
    private Long orderId;

    // ===== Coupon details =====
    
    /**
     * Coupon name.
     */
    private String couponName;

    /**
     * Coupon type.
     */
    private String type;

    /**
     * Coupon value.
     */
    private BigDecimal value;

    /**
     * Minimum spend amount.
     */
    private BigDecimal minPrice;

    /**
     * Validity start time.
     */
    private LocalDateTime startDate;

    /**
     * Validity end time.
     */
    private LocalDateTime endDate;

    /**
     * Coupon description.
     */
    private String description;
}
