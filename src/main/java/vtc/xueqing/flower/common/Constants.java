package vtc.xueqing.flower.common;

/**
 * 业务常量类
 */
public class Constants {

    /**
     * 用户会员等级
     */
    public static final String LEVEL_NORMAL = "NORMAL";
    public static final String LEVEL_VIP = "VIP";
    public static final String LEVEL_SVIP = "SVIP";

    /**
     * 商家状态
     */
    public static final String MERCHANT_STATUS_PENDING = "PENDING";
    public static final String MERCHANT_STATUS_ACTIVE = "ACTIVE";
    public static final String MERCHANT_STATUS_SUSPENDED = "SUSPENDED";
    public static final String MERCHANT_STATUS_REJECTED = "REJECTED";

    /**
     * 产品状态
     */
    public static final String PRODUCT_STATUS_ACTIVE = "ACTIVE";
    public static final String PRODUCT_STATUS_INACTIVE = "INACTIVE";
    public static final String PRODUCT_STATUS_DELETED = "DELETED";

    /**
     * 库存状态
     */
    public static final String STOCK_STATUS_IN_STOCK = "IN_STOCK";
    public static final String STOCK_STATUS_LOW_STOCK = "LOW_STOCK";
    public static final String STOCK_STATUS_OUT_OF_STOCK = "OUT_OF_STOCK";

    /**
     * 订单状态
     */
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_COMPLETED = "COMPLETED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    /**
     * 支付状态
     */
    public static final String PAYMENT_STATUS_UNPAID = "UNPAID";
    public static final String PAYMENT_STATUS_PAID = "PAID";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    /**
     * 支付方式
     */
    public static final String PAYMENT_METHOD_ALIPAY = "ALIPAY";
    public static final String PAYMENT_METHOD_WECHAT = "WECHAT";
    public static final String PAYMENT_METHOD_BALANCE = "BALANCE";

    /**
     * 优惠券类型
     */
    public static final String COUPON_TYPE_DISCOUNT = "DISCOUNT";
    public static final String COUPON_TYPE_FULL_REDUCTION = "FULL_REDUCTION";
    public static final String COUPON_TYPE_FIXED_AMOUNT = "FIXED_AMOUNT";

    /**
     * 优惠券状态
     */
    public static final String COUPON_STATUS_ACTIVE = "ACTIVE";
    public static final String COUPON_STATUS_INACTIVE = "INACTIVE";
    public static final String COUPON_STATUS_EXPIRED = "EXPIRED";

    /**
     * 用户优惠券状态
     */
    public static final String CUSTOMER_COUPON_STATUS_UNUSED = "UNUSED";
    public static final String CUSTOMER_COUPON_STATUS_USED = "USED";
    public static final String CUSTOMER_COUPON_STATUS_EXPIRED = "EXPIRED";

    /**
     * 评价状态
     */
    public static final String REVIEW_STATUS_PENDING = "PENDING";
    public static final String REVIEW_STATUS_APPROVED = "APPROVED";
    public static final String REVIEW_STATUS_REJECTED = "REJECTED";

    /**
     * 养护知识状态
     */
    public static final String CARE_KNOWLEDGE_STATUS_DRAFT = "DRAFT";
    public static final String CARE_KNOWLEDGE_STATUS_PUBLISHED = "PUBLISHED";

    /**
     * 管理员权限
     */
    public static final String ADMIN_PERMISSION_SUPER = "SUPER_ADMIN";
    public static final String ADMIN_PERMISSION_ADMIN = "ADMIN";

    /**
     * 管理员状态
     */
    public static final String ADMIN_STATUS_ACTIVE = "ACTIVE";
    public static final String ADMIN_STATUS_INACTIVE = "INACTIVE";

    /**
     * 签到奖励
     */
    public static final Integer DEFAULT_CHECK_IN_POINTS = 10;
    public static final Integer CONTINUOUS_CHECK_IN_BONUS = 5;
}
