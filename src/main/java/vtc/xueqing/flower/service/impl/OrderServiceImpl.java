package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.entity.OrderItem;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.mapper.OrderItemMapper;
import vtc.xueqing.flower.mapper.OrderMapper;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.service.OrderService;
import vtc.xueqing.flower.vo.OrderDetailVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    @Resource
    private OrderMapper orderMapper;
    
    @Resource
    private OrderItemMapper orderItemMapper;
    
    @Resource
    private ProductMapper productMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 1. 生成订单号
        order.setOrderNo(generateOrderNo());
        
        // 2. 设置订单初始状态
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentStatus("UNPAID");
        order.setStatus("PENDING");
        
        // 3. 计算订单金额（如果前端没传）
        if (order.getTotalPrice() == null || order.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
            // 从订单项中计算总价
            BigDecimal totalPrice = BigDecimal.ZERO;
            // 这里假设订单项已经在Order实体的transient字段中传入
            // 实际项目中可能需要单独的DTO
        }
        
        // 4. 计算实付金额 = 总价 - 优惠金额
        if (order.getDiscountAmount() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
        }
        order.setActualPrice(order.getTotalPrice().subtract(order.getDiscountAmount()));
        
        // 5. 保存订单
        orderMapper.insert(order);
        
        return order;
    }
    
    @Override
    public IPage<Order> getOrderPage(Page<Order> page, Long userId, Long merchId, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        
        // 条件筛选
        wrapper.eq(userId != null, Order::getUserId, userId)
                .eq(merchId != null, Order::getMerchId, merchId)
                .eq(status != null && !status.isEmpty(), Order::getStatus, status)
                .orderByDesc(Order::getOrderDate);
        
        return orderMapper.selectPage(page, wrapper);
    }
    
    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 查询订单项
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
        
        // 注意：这里需要在Order实体中添加transient字段来存储订单项
        // 或者创建OrderVO来包含订单项信息
        
        return order;
    }
    
    @Override
    public OrderDetailVO getOrderDetailById(Long orderId) {
        // 1. 查询订单基本信息（带客户和商家名称）
        OrderDetailVO orderDetail = orderMapper.selectOrderDetailById(orderId);
        if (orderDetail == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 2. 查询订单项
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
        orderDetail.setItems(orderItems);
        
        return orderDetail;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order payOrder(Long orderId, String paymentMethod) {
        // 1. 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 2. 检查订单状态
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }
        
        if ("PAID".equals(order.getPaymentStatus())) {
            throw new RuntimeException("订单已支付");
        }
        
        // 3. 根据支付方式处理（课程项目模拟实现）
        if ("BALANCE".equals(paymentMethod)) {
            // 余额支付：扣除用户余额
            Customer customer = customerMapper.selectById(order.getUserId());
            if (customer == null) {
                throw new RuntimeException("用户不存在");
            }
            
            if (customer.getBalance().compareTo(order.getActualPrice()) < 0) {
                throw new RuntimeException("余额不足");
            }
            
            // 扣除余额
            customer.setBalance(customer.getBalance().subtract(order.getActualPrice()));
            customerMapper.updateById(customer);
        } else if ("ALIPAY".equals(paymentMethod) || "WECHAT".equals(paymentMethod)) {
            // 支付宝/微信支付：模拟实现，直接标记为已支付
            // 实际项目中需要对接第三方支付接口
            System.out.println("模拟" + paymentMethod + "支付成功");
        } else {
            throw new RuntimeException("不支持的支付方式");
        }
        
        // 4. 更新订单状态
        order.setPaymentStatus("PAID");
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTime(LocalDateTime.now());
        order.setStatus("PROCESSING"); // 支付后进入处理中状态
        
        // 5. 扣减商品库存
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
        
        for (OrderItem item : orderItems) {
            Product product = productMapper.selectById(item.getProdId());
            if (product != null) {
                int newStock = product.getStock() - item.getQuantity();
                if (newStock < 0) {
                    throw new RuntimeException("商品库存不足：" + product.getName());
                }
                product.setStock(newStock);
                product.setSales(product.getSales() + item.getQuantity());
                
                // 更新库存状态
                if (newStock == 0) {
                    product.setStockStatus("OUT_OF_STOCK");
                } else if (newStock < 10) {
                    product.setStockStatus("LOW_STOCK");
                } else {
                    product.setStockStatus("IN_STOCK");
                }
                
                productMapper.updateById(product);
            }
        }
        
        orderMapper.updateById(order);
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order cancelOrder(Long orderId, String cancelReason) {
        // 1. 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 2. 检查订单状态（只有待支付和处理中的订单可以取消）
        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("订单已取消");
        }
        
        if ("COMPLETED".equals(order.getStatus())) {
            throw new RuntimeException("订单已完成，无法取消");
        }
        
        if ("SHIPPED".equals(order.getStatus())) {
            throw new RuntimeException("订单已发货，无法取消");
        }
        
        // 3. 如果已支付，需要退款
        if ("PAID".equals(order.getPaymentStatus())) {
            if ("BALANCE".equals(order.getPaymentMethod())) {
                // 余额支付：退回余额
                Customer customer = customerMapper.selectById(order.getUserId());
                if (customer != null) {
                    customer.setBalance(customer.getBalance().add(order.getActualPrice()));
                    customerMapper.updateById(customer);
                }
            }
            // 其他支付方式模拟退款
            order.setPaymentStatus("REFUNDED");
        }
        
        // 4. 恢复商品库存（如果已支付）
        if ("PAID".equals(order.getPaymentStatus())) {
            LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderItem::getOrderId, orderId);
            List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
            
            for (OrderItem item : orderItems) {
                Product product = productMapper.selectById(item.getProdId());
                if (product != null) {
                    product.setStock(product.getStock() + item.getQuantity());
                    product.setSales(Math.max(0, product.getSales() - item.getQuantity()));
                    
                    // 更新库存状态
                    if (product.getStock() > 10) {
                        product.setStockStatus("IN_STOCK");
                    } else if (product.getStock() > 0) {
                        product.setStockStatus("LOW_STOCK");
                    }
                    
                    productMapper.updateById(product);
                }
            }
        }
        
        // 5. 更新订单状态
        order.setStatus("CANCELLED");
        order.setCancelReason(cancelReason);
        orderMapper.updateById(order);
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order confirmOrder(Long orderId) {
        // 1. 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 2. 检查订单状态（只有已发货的订单可以确认收货）
        if (!"SHIPPED".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法确认收货");
        }
        
        // 3. 更新订单状态
        order.setStatus("COMPLETED");
        order.setCompletionTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order shipOrder(Long orderId) {
        // 1. 获取订单
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 2. 检查订单状态（只有处理中的订单可以发货）
        if (!"PROCESSING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法发货");
        }
        
        // 3. 更新订单状态
        order.setStatus("SHIPPED");
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        return order;
    }
    
    /**
     * 生成订单号
     * 格式：yyyyMMddHHmmss + 6位随机数
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = new Random().nextInt(900000) + 100000; // 6位随机数
        return timestamp + randomNum;
    }
}
