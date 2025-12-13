package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Order;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建的订单
     */
    Order createOrder(Order order);
    
    /**
     * 获取订单列表（分页）
     * @param page 分页信息
     * @param userId 用户ID（可选）
     * @param merchId 商家ID（可选）
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    IPage<Order> getOrderPage(Page<Order> page, Long userId, Long merchId, String status);
    
    /**
     * 根据ID获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    Order getOrderById(Long orderId);
    
    /**
     * 支付订单
     * @param orderId 订单ID
     * @param paymentMethod 支付方式：ALIPAY-支付宝，WECHAT-微信，BALANCE-余额
     * @return 支付后的订单
     */
    Order payOrder(Long orderId, String paymentMethod);
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @param cancelReason 取消原因
     * @return 取消后的订单
     */
    Order cancelOrder(Long orderId, String cancelReason);
    
    /**
     * 确认收货
     * @param orderId 订单ID
     * @return 确认后的订单
     */
    Order confirmOrder(Long orderId);
    
    /**
     * 发货（商家操作）
     * @param orderId 订单ID
     * @return 发货后的订单
     */
    Order shipOrder(Long orderId);
}
