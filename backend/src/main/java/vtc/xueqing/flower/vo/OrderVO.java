package vtc.xueqing.flower.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
    /**
     * Order placement time.
     */
import java.time.LocalDateTime;

/**
 * Order view object for admin order listings.
 * Includes joined customer and merchant names.
 */
@Data
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Order ID.
     */
    private Long id;

    /**
     * Order number.
     */
    private String orderNo;

    /**
     * User ID.
     */
    private Long userId;

    /**
     * Customer name (JOIN customers).
     */
    private String customerName;

    /**
     * Merchant ID.
     */
    private Long merchId;

    /**
     * Merchant name (JOIN merchants).
     */
    private String merchantName;

    /**
     * Order total price.
     */
    private BigDecimal totalPrice;

    /**
     * Discount amount.
     */
    private BigDecimal discountAmount;

    /**
     * Actual paid amount.
     */
    private BigDecimal actualPrice;

    /**
     * Shipping address.
     */
    private String address;

    /**
     * Recipient name.
     */
    private String receiverName;

    /**
     * Recipient phone.
     */
    private String receiverPhone;

    /**
     * Payment status.
     */
    private String paymentStatus;

    /**
     * Payment time.
     */
    private LocalDateTime paymentTime;

    /**
     * Payment method.
     */
    private String paymentMethod;

    /**
     * Order status.
     */
    private String status;

    /**
     * Shipment time.
     */
    private LocalDateTime deliveryTime;

    /**
     * Completion time.
     */
    private LocalDateTime completionTime;

    /**
     * Cancellation reason.
     */
    private String cancelReason;

    /**
     * Order remark.
     */
    private String remark;

    /**
     * Order placement time.
     */
    private LocalDateTime orderDate;

    /**
     * Created time.
     */
    private LocalDateTime createDate;

    /**
     * Updated time.
     */
    private LocalDateTime updateDate;
}
