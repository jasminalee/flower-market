package vtc.xueqing.flower.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建订单结果：父订单号 + 拆分后的子订单详情列表
 */
@Data
public class ParentOrderCreateResult implements Serializable {
    /** 父订单号（一次结算编号） */
    private String parentOrderNo;
    /** 拆分生成的子订单详情 */
    private List<OrderDetailVO> subOrders;
}
