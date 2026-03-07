package vtc.xueqing.flower.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.entity.OrderItem;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.exception.BusinessException;
import vtc.xueqing.flower.mapper.MerchantMapper;
import vtc.xueqing.flower.mapper.OrderMapper;
import vtc.xueqing.flower.mapper.OrderItemMapper;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.service.MerchantService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Merchant Service Implementation Class
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    @Resource
    private MerchantMapper merchantMapper;
    
    @Resource
    private OrderMapper orderMapper;
    
    @Resource
    private OrderItemMapper orderItemMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Resource
    private ProductMapper productMapper;
    
    @Resource
    private vtc.xueqing.flower.mapper.CouponMapper couponMapper;

    @Override
    public Merchant register(Merchant merchant) {
        // 1. Check if email already exists
        LambdaQueryWrapper<Merchant> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(Merchant::getEmail, merchant.getEmail());
        if (merchantMapper.selectCount(emailWrapper) > 0) {
            throw new BusinessException("This email has already been registered");
        }

        // 2. Check if phone number already exists
        LambdaQueryWrapper<Merchant> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(Merchant::getPhone, merchant.getPhone());
        if (merchantMapper.selectCount(phoneWrapper) > 0) {
            throw new BusinessException("This phone number has already been registered");
        }

        // 3. Encrypt password using MD5
        merchant.setPassword(SecureUtil.md5(merchant.getPassword()));
        // Merchant defaults to pending review status after registration
        merchant.setStatus(Constants.MERCHANT_STATUS_PENDING);
        merchant.setCreateDate(LocalDateTime.now());
        merchant.setUpdateDate(LocalDateTime.now());

        // 4. Save to database
        int result = merchantMapper.insert(merchant);
        if (result == 0) {
            throw new BusinessException("Registration failed");
        }

        log.info("Merchant registration successful, email: {}, status: Pending Review", merchant.getEmail());

        // 5. Return merchant object with password cleared
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant login(Merchant login) {
        // 1. Query merchant by email or phone number
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Merchant::getEmail, login.getEmail())
                .or()
                .eq(Merchant::getPhone, login.getEmail()));

        Merchant merchant = merchantMapper.selectOne(wrapper);
        if (merchant == null) {
            throw new BusinessException("Account does not exist");
        }

        // 2. Verify password (compare after MD5 encryption)
        String encryptedPassword = SecureUtil.md5(login.getPassword());
        if (!encryptedPassword.equals(merchant.getPassword())) {
            throw new BusinessException("Incorrect password");
        }

        // 3. Check merchant status
        if (Constants.MERCHANT_STATUS_PENDING.equals(merchant.getStatus())) {
            throw new BusinessException("Your merchant account is under review, please wait patiently");
        }
        if (Constants.MERCHANT_STATUS_REJECTED.equals(merchant.getStatus())) {
            throw new BusinessException("Your merchant account review was not approved");
        }
        if (Constants.MERCHANT_STATUS_SUSPENDED.equals(merchant.getStatus())) {
            throw new BusinessException("Your merchant account has been suspended");
        }

        log.info("Merchant login successful, ID: {}, Email: {}", merchant.getMerchId(), merchant.getEmail());

        // 4. Return merchant object with password cleared
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant getMerchantById(Long merchId) {
        Merchant merchant = merchantMapper.selectById(merchId);
        if (merchant == null) {
            throw new BusinessException("Merchant does not exist");
        }
        // Clear password
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Merchant updateMerchant(Merchant merchant) {
        // 1. Check if merchant exists
        Merchant existing = merchantMapper.selectById(merchant.getMerchId());
        if (existing == null) {
            throw new BusinessException("Merchant does not exist");
        }
        
        // 2. If updating password, need to encrypt
        if (merchant.getPassword() != null && !merchant.getPassword().isEmpty()) {
            merchant.setPassword(SecureUtil.md5(merchant.getPassword()));
        } else {
            // Do not update password
            merchant.setPassword(null);
        }
        
        // 3. Update
        merchant.setUpdateDate(LocalDateTime.now());
        merchantMapper.updateById(merchant);
        
        // 4. Return updated information (password cleared)
        Merchant updated = merchantMapper.selectById(merchant.getMerchId());
        updated.setPassword(null);
        return updated;
    }
    
    @Override
    public Map<String, Object> getDashboardData(Long merchId) {
        Map<String, Object> result = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusDays(7);
        LocalDateTime monthStart = now.minusDays(30);
        
        // 1. Statistical data
        Map<String, Object> stats = new HashMap<>();
        
        // Today's order count
        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, todayStart);
        long todayOrders = orderMapper.selectCount(todayWrapper);
        stats.put("todayOrders", todayOrders);
        
        // Today's sales amount
        List<Order> todayOrderList = orderMapper.selectList(todayWrapper);
        BigDecimal todaySales = todayOrderList.stream()
            .map(Order::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("todaySales", todaySales.doubleValue());
        
        // This week's order count
        LambdaQueryWrapper<Order> weekWrapper = new LambdaQueryWrapper<>();
        weekWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, weekStart);
        long weekOrders = orderMapper.selectCount(weekWrapper);
        stats.put("weekOrders", weekOrders);
        
        // This month's sales amount
        LambdaQueryWrapper<Order> monthWrapper = new LambdaQueryWrapper<>();
        monthWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, monthStart);
        List<Order> monthOrderList = orderMapper.selectList(monthWrapper);
        BigDecimal monthSales = monthOrderList.stream()
            .map(Order::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("monthSales", monthSales.doubleValue());
        
        result.put("stats", stats);
        
        // 2. Recent orders (latest 5)
        List<Map<String, Object>> recentOrders = new ArrayList<>();
        LambdaQueryWrapper<Order> recentWrapper = new LambdaQueryWrapper<>();
        recentWrapper.eq(Order::getMerchId, merchId)
                    .orderByDesc(Order::getOrderDate)
                    .last("LIMIT 5");
        List<Order> orders = orderMapper.selectList(recentWrapper);
        
        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("totalAmount", order.getTotalPrice().doubleValue());
            orderMap.put("status", order.getStatus());
            orderMap.put("statusText", getStatusText(order.getStatus()));
            
            // Get customer name
            Customer customer = customerMapper.selectById(order.getUserId());
            orderMap.put("customerName", customer != null ? customer.getName() : "Unknown");
            
            recentOrders.add(orderMap);
        }
        result.put("recentOrders", recentOrders);
        
        // 3. Best-selling products (top 5 by sales volume)
        List<Map<String, Object>> topProducts = new ArrayList<>();
        
        // Get all order IDs for completed orders of this merchant
        LambdaQueryWrapper<Order> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(Order::getMerchId, merchId)
                   .eq(Order::getStatus, Constants.ORDER_STATUS_COMPLETED);
        List<Order> completedOrders = orderMapper.selectList(completedWrapper);
        
        if (!completedOrders.isEmpty()) {
            List<Long> orderIds = completedOrders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
            
            // Count sales volume and sales amount for each product
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.in(OrderItem::getOrderId, orderIds);
            List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
            
            // Group and count by product ID
            Map<Long, Map<String, Object>> productStats = new HashMap<>();
            for (OrderItem item : items) {
                Long prodId = item.getProdId();
                productStats.putIfAbsent(prodId, new HashMap<>());
                Map<String, Object> stat = productStats.get(prodId);
                
                stat.put("name", item.getName());
                int sales = (int) stat.getOrDefault("sales", 0) + item.getQuantity();
                stat.put("sales", sales);
                
                // Use Double type to avoid type conversion errors
                double revenue = (double) stat.getOrDefault("revenue", 0.0);
                revenue += item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue();
                stat.put("revenue", revenue);
            }
            
            // Sort by sales volume, take top 5
            topProducts = productStats.values().stream()
                .sorted((a, b) -> Integer.compare((int)b.get("sales"), (int)a.get("sales")))
                .limit(5)
                .collect(Collectors.toList());
        }
        result.put("topProducts", topProducts);
        
        // 4. Sales trend (last 7 days)
        List<Map<String, Object>> salesTrend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
            
            LambdaQueryWrapper<Order> dayWrapper = new LambdaQueryWrapper<>();
            dayWrapper.eq(Order::getMerchId, merchId)
                     .ge(Order::getOrderDate, dayStart)
                     .lt(Order::getOrderDate, dayEnd);
            List<Order> dayOrders = orderMapper.selectList(dayWrapper);
            
            BigDecimal dayAmount = dayOrders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            Map<String, Object> item = new HashMap<>();
            item.put("date", (date.getMonthValue()) + "/" + date.getDayOfMonth());
            item.put("amount", dayAmount.doubleValue());
            salesTrend.add(item);
        }
        result.put("salesTrend", salesTrend);
        
        return result;
    }
    
    private String getStatusText(String status) {
        switch (status) {
            case Constants.ORDER_STATUS_SUBMITTED: return "Pending Payment";
            case Constants.ORDER_STATUS_PAID: return "Paid";
            case Constants.ORDER_STATUS_SHIPPED: return "Shipped";
            case Constants.ORDER_STATUS_COMPLETED: return "Completed";
            case Constants.ORDER_STATUS_CANCELLED: return "Cancelled";
            default: return status;
        }
    }
        
    @Override
    @Transactional(rollbackFor = Exception.class)
    public vtc.xueqing.flower.entity.Product createProduct(vtc.xueqing.flower.entity.Product product) {
        // Validate required fields
        if (product.getMerchId() == null) {
            throw new BusinessException("Merchant ID cannot be empty");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new BusinessException("Product name cannot be empty");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Product price must be greater than 0");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new BusinessException("Product stock cannot be negative");
        }
            
        // Set creation time and update time
        product.setCreateDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());
            
        // Calculate stock status based on stock quantity
        if (product.getStock() == 0) {
            product.setStockStatus(Constants.STOCK_STATUS_OUT_OF_STOCK);
        } else if (product.getStock() < 10) {
            product.setStockStatus(Constants.STOCK_STATUS_LOW_STOCK);
        } else {
            product.setStockStatus(Constants.STOCK_STATUS_IN_STOCK);
        }
            
        // Set status to active by default
        if (product.getStatus() == null) {
            product.setStatus(Constants.PRODUCT_STATUS_ACTIVE);
        }
            
        // Insert product to database
        int result = productMapper.insert(product);
        if (result == 0) {
            throw new BusinessException("Failed to create product");
        }
            
        log.info("Product created successfully, productId: {}, name: {}", product.getProdId(), product.getName());
        return product;
    }
        
    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> getMerchantProducts(
            Long merchId, Long current, Long size, String keyword) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        
        LambdaQueryWrapper<vtc.xueqing.flower.entity.Product> wrapper = 
            new LambdaQueryWrapper<>();
        wrapper.eq(vtc.xueqing.flower.entity.Product::getMerchId, merchId);
        
        // Keyword search (search product name and description)
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(vtc.xueqing.flower.entity.Product::getName, keyword)
                             .or()
                             .like(vtc.xueqing.flower.entity.Product::getDescription, keyword));
        }
        
        wrapper.orderByDesc(vtc.xueqing.flower.entity.Product::getCreateDate);
        
        return productMapper.selectPage(page, wrapper);
    }
    
    /**
     * Get merchant product list (supports more filter conditions)
     */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> getMerchantProductsWithFilter(
            Long merchId, Long current, Long size, String name, Long catId, String status) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        
        LambdaQueryWrapper<vtc.xueqing.flower.entity.Product> wrapper = 
            new LambdaQueryWrapper<>();
        wrapper.eq(vtc.xueqing.flower.entity.Product::getMerchId, merchId);
        
        // Product name search
        if (name != null && !name.trim().isEmpty()) {
            wrapper.like(vtc.xueqing.flower.entity.Product::getName, name);
        }
        
        // Category filter
        if (catId != null) {
            wrapper.eq(vtc.xueqing.flower.entity.Product::getCatId, catId);
        }
        
        // Status filter
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(vtc.xueqing.flower.entity.Product::getStatus, status);
        }
        
        wrapper.orderByDesc(vtc.xueqing.flower.entity.Product::getCreateDate);
        
        return productMapper.selectPage(page, wrapper);
    }
    
    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<Order> getMerchantOrders(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> page,
            Long merchId, String status, String orderNo, String customerName, String startDate, String endDate) {
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getMerchId, merchId);
        
        // Status filter
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }
        
        // Order Number filter (subset of OrderVO search, but here we are returning Order entity)
        if (orderNo != null && !orderNo.trim().isEmpty()) {
            wrapper.like(Order::getOrderNo, orderNo);
        }
        
        // Customer Name filter - Since Order entity doesn't have customer name, 
        // we might need to query customer IDs if we want to stay with selectPage<Order>
        if (customerName != null && !customerName.trim().isEmpty()) {
            List<Long> userIds = customerMapper.selectList(new LambdaQueryWrapper<vtc.xueqing.flower.entity.Customer>()
                    .like(vtc.xueqing.flower.entity.Customer::getName, customerName))
                    .stream().map(vtc.xueqing.flower.entity.Customer::getUserId).collect(Collectors.toList());
            if (userIds.isEmpty()) {
                // Return empty page if no customers found
                return new Page<>();
            }
            wrapper.in(Order::getUserId, userIds);
        }
        
        // Date range filter
        if (startDate != null && !startDate.trim().isEmpty()) {
            wrapper.ge(Order::getOrderDate, startDate);
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            wrapper.le(Order::getOrderDate, endDate);
        }
        
        wrapper.orderByDesc(Order::getOrderDate);
        
        return orderMapper.selectPage(page, wrapper);
    }
    
    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon> getMerchantCoupons(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Coupon> page,
            Long merchId, String status) {
        
        LambdaQueryWrapper<vtc.xueqing.flower.entity.Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(vtc.xueqing.flower.entity.Coupon::getMerchId, merchId);
        
        // Status filter
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(vtc.xueqing.flower.entity.Coupon::getStatus, status);
        }
        
        wrapper.orderByDesc(vtc.xueqing.flower.entity.Coupon::getCreateDate);
        
        return couponMapper.selectPage(page, wrapper);
    }
    
    @Override
    public vtc.xueqing.flower.entity.Coupon getMerchantCouponById(Long id) {
        return couponMapper.selectById(id);
    }
    
    @Override
    @Transactional
    public vtc.xueqing.flower.entity.Coupon createMerchantCoupon(vtc.xueqing.flower.entity.Coupon coupon) {
        coupon.setCreateDate(LocalDateTime.now());
        coupon.setUpdateDate(LocalDateTime.now());
        if (coupon.getReceivedQuantity() == null) {
            coupon.setReceivedQuantity(0);
        }
        if (coupon.getStatus() == null) {
            coupon.setStatus("ACTIVE");
        }
        couponMapper.insert(coupon);
        return coupon;
    }
    
    @Override
    @Transactional
    public vtc.xueqing.flower.entity.Coupon updateMerchantCoupon(vtc.xueqing.flower.entity.Coupon coupon) {
        vtc.xueqing.flower.entity.Coupon existing = couponMapper.selectById(coupon.getCouponId());
        if (existing == null) {
            throw new BusinessException("Coupon does not exist");
        }
        coupon.setUpdateDate(LocalDateTime.now());
        couponMapper.updateById(coupon);
        return couponMapper.selectById(coupon.getCouponId());
    }
    
    @Override
    @Transactional
    public void deleteMerchantCoupon(Long id) {
        vtc.xueqing.flower.entity.Coupon existing = couponMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("Coupon does not exist");
        }
        couponMapper.deleteById(id);
    }
}
