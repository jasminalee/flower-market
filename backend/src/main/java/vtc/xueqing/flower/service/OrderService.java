package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.dto.OrderCreateRequest;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.vo.OrderVO;
import vtc.xueqing.flower.vo.ParentOrderCreateResult;
import vtc.xueqing.flower.vo.OrderDetailVO;

/**
 * Order service interface.
 */
public interface OrderService {
    
    /**
    * Create order (supports multi-merchant split orders).
    * @param request order creation request
    * @return parent order number and child order details
    */
    ParentOrderCreateResult createOrder(OrderCreateRequest request);
    
    /**
     * Get order list (paginated).
     * @param page pagination info
     * @param userId user ID (optional)
     * @param merchId merchant ID (optional)
     * @param status order status (optional)
     * @return order list
     */
    IPage<OrderVO> getOrderPage(Page<Order> page, Long userId, Long merchId, String status);
    
    /**
     * Get order detail by ID.
     * @param orderId order ID
     * @return order detail
     */
    Order getOrderById(Long orderId);
    
    /**
     * Get order detail with customer/merchant names and items by ID.
     * @param orderId order ID
     * @return order detail VO
     */
    vtc.xueqing.flower.vo.OrderDetailVO getOrderDetailById(Long orderId);
    
    /**
     * Pay order.
     * @param orderId order ID
     * @param paymentMethod payment method: ALIPAY, WECHAT, BALANCE
     * @return paid order
     */
    Order payOrder(Long orderId, String paymentMethod);
    
    /**
     * Cancel order.
     * @param orderId order ID
     * @param cancelReason cancel reason
     * @return cancelled order
     */
    Order cancelOrder(Long orderId, String cancelReason);
    
    /**
     * Confirm receipt.
     * @param orderId order ID
     * @return confirmed order
     */
    Order confirmOrder(Long orderId);
    
    /**
     * Ship order (merchant action).
     * @param orderId order ID
     * @return shipped order
     */
    Order shipOrder(Long orderId);

    /**
     * User applies for refund.
     * @param orderId order ID
     * @param reason refund reason
     * @return order with refund applied status
     */
    Order applyRefund(Long orderId, String reason);

    /**
     * Admin/Merchant audits refund.
     * @param orderId order ID
     * @param approved true to approve, false to reject
     * @param auditRemark remark from auditor
     * @return updated order
     */
    Order auditRefund(Long orderId, boolean approved, String auditRemark);
}
