package vtc.xueqing.flower.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Create Order Request Model, Compatible with Frontend Field Naming
 */
@Data
public class OrderCreateRequest {

    private Long userId;
    /**
     * Merchant ID, Optional, Backend Will Identify From Product Information
     */
    private Long merchId;

    private String address;
    private String receiverName;
    private String receiverPhone;
    private String remark;

    /**
     * Discount Amount, Default Value is 0
     */
    private BigDecimal discountAmount;

    /**
     * Display Amount Passed by Frontend (Not Used as Final Basis, Only for Compatibility)
     */
    private BigDecimal totalAmount;

    /**
     * Payable Amount Passed by Frontend (Not Used as Final Basis, Only for Compatibility)
     */
    private BigDecimal finalAmount;

    /**
     * User Coupon ID Used (Optional)
     */
    private Long customerCouponId;

    /**
     * Order Item List
     */
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long prodId;
        private Integer quantity;
        /**
         * Unit Price Passed by Frontend (Backend Will Use Database Price)
         */
        private BigDecimal price;
    }
}
