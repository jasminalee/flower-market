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
import vtc.xueqing.flower.service.MerchantService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusDays(7);
        LocalDateTime monthStart = now.minusDays(30);
        
        // 1. 统计数据
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // 今日订单数
        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(Order::getMerchId, merchId)
                   .ge(Order::getOrderDate, todayStart);
        long todayOrders = orderMapper.selectCount(todayWrapper);
        stats.put("todayOrders", todayOrders);
        
        // 今日销售额
        java.util.List<Order> todayOrderList = orderMapper.selectList(todayWrapper);
        BigDecimal todaySales = todayOrderList.stream()
            .map(Order::getTotalAmount)
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
        java.util.List<Order> monthOrderList = orderMapper.selectList(monthWrapper);
        BigDecimal monthSales = monthOrderList.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("monthSales", monthSales.doubleValue());
        
        result.put("stats", stats);
        
        // 2. 近期订单（最近5条）
        java.util.List<java.util.Map<String, Object>> recentOrders = new java.util.ArrayList<>();
        LambdaQueryWrapper<Order> recentWrapper = new LambdaQueryWrapper<>();
        recentWrapper.eq(Order::getMerchId, merchId)
                    .orderByDesc(Order::getOrderDate)
                    .last("LIMIT 5");
        java.util.List<Order> orders = orderMapper.selectList(recentWrapper);
        
        for (Order order : orders) {
            java.util.Map<String, Object> orderMap = new java.util.HashMap<>();
            orderMap.put("orderNo", order.getOrderNo());
            orderMap.put("totalAmount", order.getTotalAmount().doubleValue());
            orderMap.put("status", order.getStatus());
            orderMap.put("statusText", getStatusText(order.getStatus()));
            
            // 获取客户名称
            Customer customer = customerMapper.selectById(order.getUserId());
            orderMap.put("customerName", customer != null ? customer.getRealName() : "未知");
            
            recentOrders.add(orderMap);
        }
        result.put("recentOrders", recentOrders);
        
        // 3. 热销商品（按销量统计，前5名）
        java.util.List<java.util.Map<String, Object>> topProducts = new java.util.ArrayList<>();
        
        // 获取该商家所有已完成订单的订单ID
        LambdaQueryWrapper<Order> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(Order::getMerchId, merchId)
                       .in(Order::getStatus, "DELIVERED", "COMPLETED");
        java.util.List<Order> completedOrders = orderMapper.selectList(completedWrapper);
        
        if (!completedOrders.isEmpty()) {
            java.util.List<Long> orderIds = completedOrders.stream()
                .map(Order::getId)
                .collect(java.util.stream.Collectors.toList());
            
            // 统计各商品的销量和销售额
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.in(OrderItem::getOrderId, orderIds);
            java.util.List<OrderItem> items = orderItemMapper.selectList(itemWrapper);
            
            // 按商品ID分组统计
            java.util.Map<Long, java.util.Map<String, Object>> productStats = new java.util.HashMap<>();
            for (OrderItem item : items) {
                Long prodId = item.getProdId();
                productStats.putIfAbsent(prodId, new java.util.HashMap<>());
                java.util.Map<String, Object> stat = productStats.get(prodId);
                
                stat.put("name", item.getName());
                int sales = (int) stat.getOrDefault("sales", 0) + item.getQuantity();
                stat.put("sales", sales);
                
                BigDecimal revenue = (BigDecimal) stat.getOrDefault("revenue", BigDecimal.ZERO);
                revenue = revenue.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                stat.put("revenue", revenue.doubleValue());
            }
            
            // 按销量排序，取前5
            topProducts = productStats.values().stream()
                .sorted((a, b) -> Integer.compare((int)b.get("sales"), (int)a.get("sales")))
                .limit(5)
                .collect(java.util.stream.Collectors.toList());
        }
        result.put("topProducts", topProducts);
        
        // 4. 销售趋势（近7天）
        java.util.List<java.util.Map<String, Object>> salesTrend = new java.util.ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
            
            LambdaQueryWrapper<Order> dayWrapper = new LambdaQueryWrapper<>();
            dayWrapper.eq(Order::getMerchId, merchId)
                     .ge(Order::getOrderDate, dayStart)
                     .lt(Order::getOrderDate, dayEnd);
            java.util.List<Order> dayOrders = orderMapper.selectList(dayWrapper);
            
            BigDecimal dayAmount = dayOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            java.util.Map<String, Object> item = new java.util.HashMap<>();
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
}
