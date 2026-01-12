package vtc.xueqing.flower.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单请求模型，兼容前端字段命名
 */
@Data
public class OrderCreateRequest {

    private Long userId;
    /**
     * 商家ID，可不传，后端将从商品信息中识别
     */
    private Long merchId;

    private String address;
    private String receiverName;
    private String receiverPhone;
    private String remark;

    /**
     * 优惠金额，缺省视为0
     */
    private BigDecimal discountAmount;

    /**
     * 前端传入的展示金额（不作为最终依据，仅为兼容字段）
     */
    private BigDecimal totalAmount;

    /**
     * 前端传入的应付金额（不作为最终依据，仅为兼容字段）
     */
    private BigDecimal finalAmount;

    /**
     * 使用的用户优惠券ID（可选）
     */
    private Long customerCouponId;

    /**
     * 订单项列表
     */
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long prodId;
        private Integer quantity;
        /**
         * 前端传入的单价（后端会以数据库价格为准）
         */
        private BigDecimal price;
    }
}
