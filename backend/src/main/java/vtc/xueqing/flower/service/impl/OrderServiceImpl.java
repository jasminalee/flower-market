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
 * Order Service Implementation Class
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
            throw new RuntimeException("Order items cannot be empty");
        }

        // Query product information, using database prices as reference
        Set<Long> prodIds = request.getItems().stream()
                .map(OrderCreateRequest.OrderItemRequest::getProdId)
                .collect(Collectors.toSet());
        List<Product> products = productMapper.selectBatchIds(prodIds);
        if (products.size() != prodIds.size()) {
            throw new RuntimeException("Some products do not exist or have been delisted");
        }

        // Group by merchant where products belong, perform order splitting
        Map<Long, List<OrderCreateRequest.OrderItemRequest>> merchItems = request.getItems().stream()
                .collect(Collectors.groupingBy(item -> {
                    Product product = products.stream()
                            .filter(p -> p.getProdId().equals(item.getProdId()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Product does not exist"));
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
        // Only pagination parameters are passed through from external Page<Order>, query returns OrderVO (includes merchant name)
        Page<OrderVO> voPage = new Page<>(page.getCurrent(), page.getSize());
        return orderMapper.selectOrdersWithMerchant(voPage, userId, merchId, status);
    }
    
    @Override
    public Order getOrderById(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }
        
        // Query order items
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
        
        // Note: Need to add transient field to Order entity to store order items
        // Or create OrderVO to include order item information
        
        return order;
    }
    
    @Override
    public OrderDetailVO getOrderDetailById(Long orderId) {
        // 1. Query basic order information (with customer and merchant names)
        OrderDetailVO orderDetail = orderMapper.selectOrderDetailById(orderId);
        if (orderDetail == null) {
            throw new RuntimeException("Order does not exist");
        }
        
        // 2. Query order items
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
        orderDetail.setItems(orderItems);
        
        return orderDetail;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order payOrder(Long orderId, String paymentMethod) {
        // 1. Get order
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }
        
        // 2. Check order status
        if (!Constants.ORDER_STATUS_SUBMITTED.equals(order.getStatus())) {
            throw new RuntimeException("Order status is incorrect, payment cannot be processed");
        }
        
        if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
            throw new RuntimeException("Order already paid");
        }
        
        // 3. Process according to payment method (course project simulation implementation)
        if (Constants.PAYMENT_METHOD_BALANCE.equals(paymentMethod)) {
            // Balance payment: deduct user balance
            Customer customer = customerMapper.selectById(order.getUserId());
            if (customer == null) {
                throw new RuntimeException("User does not exist");
            }
            
            if (customer.getBalance().compareTo(order.getActualPrice()) < 0) {
                throw new RuntimeException("Insufficient balance");
            }
            
            // Deduct balance
            customer.setBalance(customer.getBalance().subtract(order.getActualPrice()));
            customerMapper.updateById(customer);
        } else if (Constants.PAYMENT_METHOD_ALIPAY.equals(paymentMethod) || Constants.PAYMENT_METHOD_WECHAT.equals(paymentMethod)) {
            // Alipay/WeChat payment: simulation implementation, directly marked as paid
            // In actual projects, need to integrate with third-party payment interfaces
            System.out.println("Simulating " + paymentMethod + " payment success");
        } else {
            throw new RuntimeException("Unsupported payment method");
        }
        
        // 4. Update order status
        order.setPaymentStatus(Constants.PAYMENT_STATUS_PAID);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTime(LocalDateTime.now());
        order.setStatus(Constants.ORDER_STATUS_PAID);
        
        // 5. Reduce product inventory
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
        
        for (OrderItem item : orderItems) {
            Product product = productMapper.selectById(item.getProdId());
            if (product != null) {
                int newStock = product.getStock() - item.getQuantity();
                if (newStock < 0) {
                    throw new RuntimeException("Insufficient product inventory: " + product.getName());
                }
                product.setStock(newStock);
                product.setSales(product.getSales() + item.getQuantity());
                
                // Update inventory status
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
        // 1. Get order
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }
        
        // 2. Check order status (only pending payment and processing orders can be cancelled)
        if (Constants.ORDER_STATUS_CANCELLED.equals(order.getStatus())) {
            throw new RuntimeException("Order already cancelled");
        }
        
        if (Constants.ORDER_STATUS_COMPLETED.equals(order.getStatus())) {
            throw new RuntimeException("Order completed, cannot be cancelled");
        }
        
        if (Constants.ORDER_STATUS_SHIPPED.equals(order.getStatus())) {
            throw new RuntimeException("Order shipped, cannot be cancelled");
        }
        
        // 3. If paid, refund is needed
        if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
            if (Constants.PAYMENT_METHOD_BALANCE.equals(order.getPaymentMethod())) {
                // Balance payment: refund balance
                Customer customer = customerMapper.selectById(order.getUserId());
                if (customer != null) {
                    customer.setBalance(customer.getBalance().add(order.getActualPrice()));
                    customerMapper.updateById(customer);
                }
            }
            // Other payment methods simulate refund
            order.setPaymentStatus(Constants.PAYMENT_STATUS_REFUNDED);
        }
        
        // 4. Restore product inventory (if paid)
        if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
            LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderItem::getOrderId, orderId);
            List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
            
            for (OrderItem item : orderItems) {
                Product product = productMapper.selectById(item.getProdId());
                if (product != null) {
                    product.setStock(product.getStock() + item.getQuantity());
                    product.setSales(Math.max(0, product.getSales() - item.getQuantity()));
                    
                    // Update inventory status
                    if (product.getStock() > 10) {
                        product.setStockStatus("IN_STOCK");
                    } else if (product.getStock() > 0) {
                        product.setStockStatus("LOW_STOCK");
                    }
                    
                    productMapper.updateById(product);
                }
            }
        }
        
        // 5. Update order status
        order.setStatus(Constants.ORDER_STATUS_CANCELLED);
        order.setCancelReason(cancelReason);
        orderMapper.updateById(order);
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order confirmOrder(Long orderId) {
        // 1. Get order
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }
        
        // 2. Check order status (only shipped orders can be confirmed received)
        if (!Constants.ORDER_STATUS_SHIPPED.equals(order.getStatus())) {
            throw new RuntimeException("Order status is incorrect, cannot confirm receipt");
        }
        
        // 3. Update order status
        order.setStatus(Constants.ORDER_STATUS_COMPLETED);
        order.setCompletionTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        // 4. Give user consumption points (1 yuan = 1 point)
        Customer customer = customerMapper.selectById(order.getUserId());
        if (customer != null && order.getTotalPrice() != null) {
            int earnedPoints = order.getTotalPrice().intValue(); // Consumption amount rounded down as points
            Integer currentPoints = customer.getPoints() == null ? 0 : customer.getPoints();
            customer.setPoints(currentPoints + earnedPoints);
            customerMapper.updateById(customer);
        }
        
        return order;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order shipOrder(Long orderId) {
        // 1. Get order
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }
        
        // 2. Check order status (only processing orders can be shipped)
        if (!Constants.ORDER_STATUS_PAID.equals(order.getStatus())) {
            throw new RuntimeException("Order status is incorrect, cannot ship");
        }
        
        // 3. Update order status
        order.setStatus(Constants.ORDER_STATUS_SHIPPED);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateById(order);
        
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order applyRefund(Long orderId, String reason) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }

        // Only paid or shipped orders can apply for refund
        if (!Constants.ORDER_STATUS_PAID.equals(order.getStatus()) && !Constants.ORDER_STATUS_SHIPPED.equals(order.getStatus())) {
            throw new RuntimeException("Order status is incorrect, cannot apply for refund");
        }

        order.setStatus(Constants.ORDER_STATUS_REFUND_APPLIED);
        order.setCancelReason(reason); // Reuse cancelReason as refund reason
        orderMapper.updateById(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order auditRefund(Long orderId, boolean approved, String auditRemark) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("Order does not exist");
        }

        if (!Constants.ORDER_STATUS_REFUND_APPLIED.equals(order.getStatus())) {
            throw new RuntimeException("Order is not in refund applied status");
        }

        if (approved) {
            // Logic for refunding money (similar to cancelOrder)
            if (Constants.PAYMENT_STATUS_PAID.equals(order.getPaymentStatus())) {
                if (Constants.PAYMENT_METHOD_BALANCE.equals(order.getPaymentMethod())) {
                    Customer customer = customerMapper.selectById(order.getUserId());
                    if (customer != null) {
                        customer.setBalance(customer.getBalance().add(order.getActualPrice()));
                        customerMapper.updateById(customer);
                    }
                }
                order.setPaymentStatus(Constants.PAYMENT_STATUS_REFUNDED);
            }

            // Restore inventory
            LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderItem::getOrderId, orderId);
            List<OrderItem> orderItems = orderItemMapper.selectList(wrapper);
            
            for (OrderItem item : orderItems) {
                Product product = productMapper.selectById(item.getProdId());
                if (product != null) {
                    product.setStock(product.getStock() + item.getQuantity());
                    product.setSales(Math.max(0, product.getSales() - item.getQuantity()));
                    
                    if (product.getStock() > 10) {
                        product.setStockStatus(Constants.STOCK_STATUS_IN_STOCK);
                    } else if (product.getStock() > 0) {
                        product.setStockStatus(Constants.STOCK_STATUS_LOW_STOCK);
                    } else {
                        product.setStockStatus(Constants.STOCK_STATUS_OUT_OF_STOCK);
                    }
                    productMapper.updateById(product);
                }
            }

            order.setStatus(Constants.ORDER_STATUS_REFUNDED);
        } else {
            // Revert status to previous (approximated, since we didn't store previous status. Typically PAID)
            // For now, move to REFUND_REJECTED
            order.setStatus(Constants.ORDER_STATUS_REFUND_REJECTED);
        }
        
        order.setRemark(auditRemark); // Reuse remark or similar for audit comments
        orderMapper.updateById(order);
        return order;
    }
    
    /**
     * Generate order number
     * Format: yyyyMMddHHmmss + 6-digit random number
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = new Random().nextInt(900000) + 100000; // 6-digit random number
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
