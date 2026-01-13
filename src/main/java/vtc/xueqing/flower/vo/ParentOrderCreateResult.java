package vtc.xueqing.flower.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Order creation result: parent order number with split child order details.
 */
@Data
public class ParentOrderCreateResult implements Serializable {
    /** Parent order number (single checkout identifier). */
    private String parentOrderNo;
    /** Details of generated child orders. */
    private List<OrderDetailVO> subOrders;
}
