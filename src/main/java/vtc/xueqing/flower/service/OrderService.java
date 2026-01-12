package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.dto.OrderCreateRequest;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.vo.OrderVO;
import vtc.xueqing.flower.vo.ParentOrderCreateResult;
import vtc.xueqing.flower.vo.OrderDetailVO;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
    * 创建订单（支持多商家拆单）
    * @param request 创建订单请求
    * @return 父订单号及子订单详情列表
    */
    ParentOrderCreateResult createOrder(OrderCreateRequest request);
    
    /**
     * 获取订单列表（分页）
     * @param page 分页信息
     * @param userId 用户ID（可选）
     * @param merchId 商家ID（可选）
     * @param status 订单状态（可选）
     * @return 订单列表
     */
    IPage<OrderVO> getOrderPage(Page<Order> page, Long userId, Long merchId, String status);
    
    /**
     * 根据ID获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    Order getOrderById(Long orderId);
    
    /**
     * 根据ID获取订单详情（带客户和商家名称及订单项）
     * @param orderId 订单ID
     * @return 订单详情VO
     */
    vtc.xueqing.flower.vo.OrderDetailVO getOrderDetailById(Long orderId);
    
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
