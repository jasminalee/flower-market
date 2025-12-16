package vtc.xueqing.flower.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * 商家服务实现类
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
        // 1. 检查邮箱是否已存在
        LambdaQueryWrapper<Merchant> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(Merchant::getEmail, merchant.getEmail());
        if (merchantMapper.selectCount(emailWrapper) > 0) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 2. 检查手机号是否已存在
        LambdaQueryWrapper<Merchant> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(Merchant::getPhone, merchant.getPhone());
        if (merchantMapper.selectCount(phoneWrapper) > 0) {
            throw new BusinessException("该手机号已被注册");
        }

        // 3. 使用MD5加密密码
        merchant.setPassword(SecureUtil.md5(merchant.getPassword()));
        // 商家注册后默认为待审核状态
        merchant.setStatus(Constants.MERCHANT_STATUS_PENDING);
        merchant.setCreateDate(LocalDateTime.now());
        merchant.setUpdateDate(LocalDateTime.now());

        // 4. 保存到数据库
        int result = merchantMapper.insert(merchant);
        if (result == 0) {
            throw new BusinessException("注册失败");
        }

        log.info("商家注册成功，邮箱：{}, 状态：待审核", merchant.getEmail());

        // 5. 返回密码置空的merchant对象
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant login(Merchant login) {
        // 1. 根据邮箱或手机号查询商家
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Merchant::getEmail, login.getEmail())
                .or()
                .eq(Merchant::getPhone, login.getEmail()));

        Merchant merchant = merchantMapper.selectOne(wrapper);
        if (merchant == null) {
            throw new BusinessException("账号不存在");
        }

        // 2. 验证密码（MD5加密后比较）
        String encryptedPassword = SecureUtil.md5(login.getPassword());
        if (!encryptedPassword.equals(merchant.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 3. 检查商家状态
        if (Constants.MERCHANT_STATUS_PENDING.equals(merchant.getStatus())) {
            throw new BusinessException("您的商家账号正在审核中，请耐心等待");
        }
        if (Constants.MERCHANT_STATUS_REJECTED.equals(merchant.getStatus())) {
            throw new BusinessException("您的商家账号审核未通过");
        }
        if (Constants.MERCHANT_STATUS_SUSPENDED.equals(merchant.getStatus())) {
            throw new BusinessException("您的商家账号已被暂停");
        }

        log.info("商家登录成功，ID：{}, 邮箱：{}", merchant.getMerchId(), merchant.getEmail());

        // 4. 返回密码置空的merchant对象
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    public Merchant getMerchantById(Long merchId) {
        Merchant merchant = merchantMapper.selectById(merchId);
        if (merchant == null) {
            throw new BusinessException("商家不存在");
        }
        // 密码置空
        merchant.setPassword(null);
        return merchant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Merchant updateMerchant(Merchant merchant) {
        // 1. 检查商家是否存在
        Merchant existing = merchantMapper.selectById(merchant.getMerchId());
        if (existing == null) {
            throw new BusinessException("商家不存在");
        }
        
        // 2. 如果更新密码，需要加密
        if (merchant.getPassword() != null && !merchant.getPassword().isEmpty()) {
            merchant.setPassword(SecureUtil.md5(merchant.getPassword()));
        } else {
            // 不更新密码
            merchant.setPassword(null);
        }
        
        // 3. 更新
        merchant.setUpdateDate(LocalDateTime.now());
        merchantMapper.updateById(merchant);
        
        // 4. 返回更新后的信息（密码置空）
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
        
        // 1. 统计数据
        Map<String, Object> stats = new HashMap<>();
        
        // 今日订单数
        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, todayStart);
        long todayOrders = orderMapper.selectCount(todayWrapper);
        stats.put("todayOrders", todayOrders);
        
        // 今日销售额
        List<Order> todayOrderList = orderMapper.selectList(todayWrapper);
        BigDecimal todaySales = todayOrderList.stream()
            .map(Order::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("todaySales", todaySales.doubleValue());
        
        // 本周订单数
        LambdaQueryWrapper<Order> weekWrapper = new LambdaQueryWrapper<>();
        weekWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, weekStart);
        long weekOrders = orderMapper.selectCount(weekWrapper);
        stats.put("weekOrders", weekOrders);
        
        // 本月销售额
        LambdaQueryWrapper<Order> monthWrapper = new LambdaQueryWrapper<>();
        monthWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, monthStart);
        List<Order> monthOrderList = orderMapper.selectList(monthWrapper);
        BigDecimal monthSales = monthOrderList.stream()
            .map(Order::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("monthSales", monthSales.doubleValue());
        
        result.put("stats", stats);
        
        // 2. 近期订单（最近5条）
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
            
            // 获取客户名称
            Customer customer = customerMapper.selectById(order.getUserId());
            orderMap.put("customerName", customer != null ? customer.getName() : "未知");
            
            recentOrders.add(orderMap);
        }
        result.put("recentOrders", recentOrders);
        
        // 3. 热销商品（按销量统计，前5名）
        List<Map<String, Object>> topProducts = new ArrayList<>();
        
        // 获取该商家所有已完成订单的订单ID
        LambdaQueryWrapper<Order> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(Order::getMerchId, merchId)
                       .in(Order::getStatus, "DELIVERED", "COMPLETED");
        List<Order> completedOrders = orderMapper.selectList(completedWrapper);
        
        if (!completedOrders.isEmpty()) {
            List<Long> orderIds = completedOrders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());
            
            // 统计各商品的销量和销售额
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.in(OrderItem::getOrderId, orderIds);
            List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
            
            // 按商品ID分组统计
            Map<Long, Map<String, Object>> productStats = new HashMap<>();
            for (OrderItem item : items) {
                Long prodId = item.getProdId();
                productStats.putIfAbsent(prodId, new HashMap<>());
                Map<String, Object> stat = productStats.get(prodId);
                
                stat.put("name", item.getName());
                int sales = (int) stat.getOrDefault("sales", 0) + item.getQuantity();
                stat.put("sales", sales);
                
                // 使用Double类型避免类型转换错误
                double revenue = (double) stat.getOrDefault("revenue", 0.0);
                revenue += item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())).doubleValue();
                stat.put("revenue", revenue);
            }
            
            // 按销量排序，取前5
            topProducts = productStats.values().stream()
                .sorted((a, b) -> Integer.compare((int)b.get("sales"), (int)a.get("sales")))
                .limit(5)
                .collect(Collectors.toList());
        }
        result.put("topProducts", topProducts);
        
        // 4. 销售趋势（近7天）
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
            case "PENDING": return "待支付";
            case "PAID": return "已支付";
            case "SHIPPED": return "已发货";
            case "DELIVERED": return "已完成";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }
    
    @Override
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> getMerchantProducts(
            Long merchId, Long current, Long size, String keyword) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        
        LambdaQueryWrapper<vtc.xueqing.flower.entity.Product> wrapper = 
            new LambdaQueryWrapper<>();
        wrapper.eq(vtc.xueqing.flower.entity.Product::getMerchId, merchId);
        
        // 关键词搜索（搜索商品名称和描述）
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(vtc.xueqing.flower.entity.Product::getName, keyword)
                             .or()
                             .like(vtc.xueqing.flower.entity.Product::getDescription, keyword));
        }
        
        wrapper.orderByDesc(vtc.xueqing.flower.entity.Product::getCreateDate);
        
        return productMapper.selectPage(page, wrapper);
    }
    
    /**
     * 获取商家商品列表（支持更多筛选条件）
     */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> getMerchantProductsWithFilter(
            Long merchId, Long current, Long size, String name, Long catId, String status) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        
        LambdaQueryWrapper<vtc.xueqing.flower.entity.Product> wrapper = 
            new LambdaQueryWrapper<>();
        wrapper.eq(vtc.xueqing.flower.entity.Product::getMerchId, merchId);
        
        // 商品名称搜索
        if (name != null && !name.trim().isEmpty()) {
            wrapper.like(vtc.xueqing.flower.entity.Product::getName, name);
        }
        
        // 分类筛选
        if (catId != null) {
            wrapper.eq(vtc.xueqing.flower.entity.Product::getCatId, catId);
        }
        
        // 状态筛选
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(vtc.xueqing.flower.entity.Product::getStatus, status);
        }
        
        wrapper.orderByDesc(vtc.xueqing.flower.entity.Product::getCreateDate);
        
        return productMapper.selectPage(page, wrapper);
    }
    
    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<Order> getMerchantOrders(
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> page,
            Long merchId, String status) {
        
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getMerchId, merchId);
        
        // 状态筛选
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Order::getStatus, status);
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
        
        // 状态筛选
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
            throw new BusinessException("优惠券不存在");
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
            throw new BusinessException("优惠券不存在");
        }
        couponMapper.deleteById(id);
    }
}
