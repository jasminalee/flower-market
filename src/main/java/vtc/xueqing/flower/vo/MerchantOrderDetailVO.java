package vtc.xueqing.flower.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商家端订单详情视图
 */
@Data
public class MerchantOrderDetailVO implements Serializable {
    private Long id;
    private String orderNo;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private String customerName;
    private String customerPhone;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private BigDecimal itemsTotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private LocalDateTime shipTime;
    private String courier;
    private String trackingNo;
    private List<Item> items;

    @Data
    public static class Item {
        private String productName;
        private String productImage;
        private BigDecimal price;
        private Integer quantity;
    }
}
