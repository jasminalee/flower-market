package vtc.xueqing.flower.common;

/**
 * Business constants.
 */
public class Constants {

    /**
     * Customer membership levels.
     */
    public static final String LEVEL_NORMAL = "NORMAL";
    public static final String LEVEL_VIP = "VIP";
    public static final String LEVEL_SVIP = "SVIP";

    /**
     * Merchant statuses.
     */
    public static final String MERCHANT_STATUS_PENDING = "PENDING";
    public static final String MERCHANT_STATUS_ACTIVE = "ACTIVE";
    public static final String MERCHANT_STATUS_SUSPENDED = "SUSPENDED";
    public static final String MERCHANT_STATUS_REJECTED = "REJECTED";

    /**
     * Product statuses.
     */
    public static final String PRODUCT_STATUS_ACTIVE = "ACTIVE";
    public static final String PRODUCT_STATUS_INACTIVE = "INACTIVE";
    public static final String PRODUCT_STATUS_DELETED = "DELETED";

    /**
     * Inventory statuses.
     */
    public static final String STOCK_STATUS_IN_STOCK = "IN_STOCK";
    public static final String STOCK_STATUS_LOW_STOCK = "LOW_STOCK";
    public static final String STOCK_STATUS_OUT_OF_STOCK = "OUT_OF_STOCK";

    /**
     * Order statuses.
     */
    /** Order submitted */
    public static final String ORDER_STATUS_SUBMITTED = "SUBMITTED";
    /** Payment successful */
    public static final String ORDER_STATUS_PAID = "PAID";
    /** Merchant shipped */
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    /** Transaction completed (confirmed receipt) */
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    /** Order cancelled */
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    /**
     * Payment statuses.
     */
    public static final String PAYMENT_STATUS_UNPAID = "UNPAID";
    public static final String PAYMENT_STATUS_PAID = "PAID";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    /**
     * Payment methods.
     */
    public static final String PAYMENT_METHOD_ALIPAY = "ALIPAY";
    public static final String PAYMENT_METHOD_WECHAT = "WECHAT";
    public static final String PAYMENT_METHOD_BALANCE = "BALANCE";

    /**
     * Coupon types.
     */
    public static final String COUPON_TYPE_DISCOUNT = "DISCOUNT";
    public static final String COUPON_TYPE_FULL_REDUCTION = "FULL_REDUCTION";
    public static final String COUPON_TYPE_FIXED_AMOUNT = "FIXED_AMOUNT";

    /**
     * Coupon statuses.
     */
    public static final String COUPON_STATUS_ACTIVE = "ACTIVE";
    public static final String COUPON_STATUS_INACTIVE = "INACTIVE";
    public static final String COUPON_STATUS_EXPIRED = "EXPIRED";

    /**
     * Customer coupon statuses.
     */
    public static final String CUSTOMER_COUPON_STATUS_UNUSED = "UNUSED";
    public static final String CUSTOMER_COUPON_STATUS_USED = "USED";
    public static final String CUSTOMER_COUPON_STATUS_EXPIRED = "EXPIRED";

    /**
     * Review statuses.
     */
    public static final String REVIEW_STATUS_PENDING = "PENDING";
    public static final String REVIEW_STATUS_APPROVED = "APPROVED";
    public static final String REVIEW_STATUS_REJECTED = "REJECTED";

    /**
     * Care knowledge statuses.
     */
    public static final String CARE_KNOWLEDGE_STATUS_DRAFT = "DRAFT";
    public static final String CARE_KNOWLEDGE_STATUS_PUBLISHED = "PUBLISHED";

    /**
     * Administrator permissions.
     */
    public static final String ADMIN_PERMISSION_SUPER = "SUPER_ADMIN";
    public static final String ADMIN_PERMISSION_ADMIN = "ADMIN";

    /**
     * Administrator statuses.
     */
    public static final String ADMIN_STATUS_ACTIVE = "ACTIVE";
    public static final String ADMIN_STATUS_INACTIVE = "INACTIVE";

    /**
     * Check-in rewards.
     */
    public static final Integer DEFAULT_CHECK_IN_POINTS = 10;
    public static final Integer CONTINUOUS_CHECK_IN_BONUS = 5;
}
