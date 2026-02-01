package vtc.xueqing.flower.vo;

import lombok.Data;
import vtc.xueqing.flower.entity.OrderItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order detail view object for displaying order details.
 * Includes customer name, merchant name, and order items.
 */
@Data
public class OrderDetailVO implements Serializable {

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
     * Customer phone (JOIN customers).
     */
    private String customerPhone;

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

    /**
     * Order item list.
     */
    private List<OrderItem> items;
}
