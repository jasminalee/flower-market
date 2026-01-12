package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.entity.OrderItem;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.dto.OrderCreateRequest;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.mapper.OrderItemMapper;
import vtc.xueqing.flower.mapper.OrderMapper;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.service.OrderService;
import vtc.xueqing.flower.vo.OrderDetailVO;
import vtc.xueqing.flower.vo.OrderVO;
import vtc.xueqing.flower.vo.ParentOrderCreateResult;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    public ParentOrderCreateResult createOrder(OrderCreateRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getItems())) {
            throw new RuntimeException("订单商品不能为空");
        }

        // 查询商品信息，以数据库价格为准
        Set<Long> prodIds = request.getItems().stream()
                .map(OrderCreateRequest.OrderItemRequest::getProdId)
                .collect(Collectors.toSet());
        List<Product> products = productMapper.selectBatchIds(prodIds);
        if (products.size() != prodIds.size()) {
            throw new RuntimeException("部分商品不存在或已下架");
        }

        // 以商品所在商家分组，执行拆单
        Map<Long, List<OrderCreateRequest.OrderItemRequest>> merchItems = request.getItems().stream()
                .collect(Collectors.groupingBy(item -> {
                    Product product = products.stream()
                            .filter(p -> p.getProdId().equals(item.getProdId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("商品不存在"));
                    return product.getMerchId();
                }));

        LocalDateTime now = LocalDateTime.now();
        BigDecimal totalAll = merchItems.values().stream()
                .flatMap(List::stream)
                .map(i -> {
                    Product p = products.stream().filter(prod -> prod.getProdId().equals(i.getProdId())).findFirst().orElse(null);
                    return p == null ? BigDecimal.ZERO : p.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discountTotal = request.getDiscountAmount() == null ? BigDecimal.ZERO : request.getDiscountAmount();
        if (discountTotal.compareTo(BigDecimal.ZERO) < 0) {
            discountTotal = BigDecimal.ZERO;
        }

        String parentOrderNo = generateParentOrderNo();
        List<OrderDetailVO> subOrders = new ArrayList<>();

        for (Map.Entry<Long, List<OrderCreateRequest.OrderItemRequest>> entry : merchItems.entrySet()) {
            Long merchId = entry.getKey();
            List<OrderCreateRequest.OrderItemRequest> items = entry.getValue();

            BigDecimal merchSubtotal = items.stream()
                    .map(i -> {
                        Product p = products.stream().filter(prod -> prod.getProdId().equals(i.getProdId())).findFirst().orElse(null);
                        return p == null ? BigDecimal.ZERO : p.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal merchDiscount = allocateDiscount(merchSubtotal, totalAll, discountTotal);
            BigDecimal merchActual = merchSubtotal.subtract(merchDiscount);
            if (merchActual.compareTo(BigDecimal.ZERO) < 0) {
                merchActual = BigDecimal.ZERO;
            }

            Order order = new Order();
            order.setOrderNo(generateOrderNo());
            order.setUserId(request.getUserId());
            order.setMerchId(merchId);
            order.setOrderDate(now);
            order.setPaymentStatus(Constants.PAYMENT_STATUS_UNPAID);
            order.setStatus(Constants.ORDER_STATUS_SUBMITTED);
            order.setAddress(request.getAddress());
            order.setReceiverName(request.getReceiverName());
            order.setReceiverPhone(request.getReceiverPhone());
            order.setRemark(request.getRemark());
            order.setTotalPrice(merchSubtotal);
            order.setDiscountAmount(merchDiscount);
            order.setActualPrice(merchActual);
            order.setCreateDate(now);
            order.setUpdateDate(now);

            orderMapper.insert(order);

            Long orderId = order.getId();
            for (OrderCreateRequest.OrderItemRequest item : items) {
                Product product = products.stream()
                        .filter(p -> p.getProdId().equals(item.getProdId()))
                        .findFirst()
                        .orElse(null);
                if (product == null) {
                    continue;
                }
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProdId(product.getProdId());
                orderItem.setName(product.getName());
                orderItem.setMainImage(product.getMainImage());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setUnitPrice(product.getPrice());
                orderItem.setTotalPrice(product.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())));
                orderItem.setCreateDate(now);
                orderItemMapper.insert(orderItem);
            }

            subOrders.add(getOrderDetailById(orderId));
        }

        ParentOrderCreateResult result = new ParentOrderCreateResult();
        result.setParentOrderNo(parentOrderNo);
        result.setSubOrders(subOrders);
        return result;
    }
    
    @Override
    public IPage<OrderVO> getOrderPage(Page<Order> page, Long userId, Long merchId, String status) {
        // 仅分页参数从外部 Page<Order> 透传，查询返回 OrderVO（含商家名）
        Page<OrderVO> voPage = new Page<>(page.getCurrent(), page.getSize());
        return orderMapper.selectOrdersWithMerchant(voPage, userId, merchId, status);
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
        if (!Constants.ORDER_STATUS_SUBMITTED.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法支付");
        }
        
        if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
            throw new RuntimeException("订单已支付");
        }
        
        // 3. 根据支付方式处理（课程项目模拟实现）
        if (Constants.PAYMENT_METHOD_BALANCE.equals(paymentMethod)) {
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
        } else if (Constants.PAYMENT_METHOD_ALIPAY.equals(paymentMethod) || Constants.PAYMENT_METHOD_WECHAT.equals(paymentMethod)) {
            // 支付宝/微信支付：模拟实现，直接标记为已支付
            // 实际项目中需要对接第三方支付接口
            System.out.println("模拟" + paymentMethod + "支付成功");
        } else {
            throw new RuntimeException("不支持的支付方式");
        }
        
        // 4. 更新订单状态
        order.setPaymentStatus(Constants.PAYMENT_STATUS_PAID);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTime(LocalDateTime.now());
        order.setStatus(Constants.ORDER_STATUS_PAID);
        
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
        if (Constants.ORDER_STATUS_CANCELLED.equals(order.getStatus())) {
            throw new RuntimeException("订单已取消");
        }
        
        if (Constants.ORDER_STATUS_COMPLETED.equals(order.getStatus())) {
            throw new RuntimeException("订单已完成，无法取消");
        }
        
        if (Constants.ORDER_STATUS_SHIPPED.equals(order.getStatus())) {
            throw new RuntimeException("订单已发货，无法取消");
        }
        
        // 3. 如果已支付，需要退款
        if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
            if (Constants.PAYMENT_METHOD_BALANCE.equals(order.getPaymentMethod())) {
                // 余额支付：退回余额
                Customer customer = customerMapper.selectById(order.getUserId());
                if (customer != null) {
                    customer.setBalance(customer.getBalance().add(order.getActualPrice()));
                    customerMapper.updateById(customer);
                }
            }
            // 其他支付方式模拟退款
            order.setPaymentStatus(Constants.PAYMENT_STATUS_REFUNDED);
        }
        
        // 4. 恢复商品库存（如果已支付）
        if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
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
        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
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
        if (!Constants.ORDER_STATUS_SHIPPED.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法确认收货");
        }
        
        // 3. 更新订单状态
        order.setStatus(Constants.ORDER_STATUS_COMPLETED);
        order.setCompletionTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        // 4. 给用户增加消费积分（1元=1积分）
        Customer customer = customerMapper.selectById(order.getUserId());
        if (customer != null && order.getTotalPrice() != null) {
            int earnedPoints = order.getTotalPrice().intValue(); // 消费金额取整作为积分
            Integer currentPoints = customer.getPoints() == null ? 0 : customer.getPoints();
            customer.setPoints(currentPoints + earnedPoints);
            customerMapper.updateById(customer);
        }
        
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
        if (!Constants.ORDER_STATUS_PAID.equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法发货");
        }
        
        // 3. 更新订单状态
        order.setStatus(Constants.ORDER_STATUS_SHIPPED);
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

    private String generateParentOrderNo() {
        return "P" + generateOrderNo();
    }

    private BigDecimal allocateDiscount(BigDecimal partAmount, BigDecimal totalAmount, BigDecimal discountTotal) {
        if (discountTotal.compareTo(BigDecimal.ZERO) <= 0 || totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal ratio = partAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP);
        return discountTotal.multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
