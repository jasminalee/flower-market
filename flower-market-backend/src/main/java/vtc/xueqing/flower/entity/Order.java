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
 * Order entity class
 * Corresponds to database table: orders
 */
@Data
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Order ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Order number
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Merchant ID
     */
    @TableField("merch_id")
    private Long merchId;

    /**
     * Order time
     */
    @TableField("order_date")
    private LocalDateTime orderDate;

    /**
     * Total order price
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * Discount amount
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * Actual payment amount
     */
    @TableField("actual_price")
    private BigDecimal actualPrice;

    /**
     * Shipping address
     */
    @TableField("address")
    private String address;

    /**
     * Recipient name
     */
    @TableField("receiver_name")
    private String receiverName;

    /**
     * Recipient phone
     */
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * Payment status: UNPAID-unpaid, PAID-paid, REFUNDED-refunded
     */
    @TableField("payment_status")
    private String paymentStatus;

    /**
     * Payment time
     */
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    /**
     * Payment method: ALIPAY-Alipay, WECHAT-WeChat, BALANCE-balance
     */
    @TableField("payment_method")
    private String paymentMethod;

    /**
     * Order status: SUBMITTED-order submitted/pending payment, PAID-paid/pending shipment, SHIPPED-shipped/pending receipt, COMPLETED-transaction completed, CANCELLED-cancelled
     */
    @TableField("status")
    private String status;

    /**
     * Delivery time
     */
    @TableField("delivery_time")
    private LocalDateTime deliveryTime;

    /**
     * Completion time
     */
    @TableField("completion_time")
    private LocalDateTime completionTime;

    /**
     * Cancellation reason
     */
    @TableField("cancel_reason")
    private String cancelReason;

    /**
     * Order remarks
     */
    @TableField("remark")
    private String remark;

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
