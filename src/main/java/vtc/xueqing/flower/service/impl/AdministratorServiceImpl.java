package vtc.xueqing.flower.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Administrator;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.mapper.AdministratorMapper;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.mapper.MerchantMapper;
import vtc.xueqing.flower.service.AdministratorService;

import javax.annotation.Resource;

/**
 * 管理员服务实现类
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {
    
    @Resource
    private AdministratorMapper administratorMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Resource
    private MerchantMapper merchantMapper;
    
    @Override
    public Administrator login(String email, String password) {
        // 1. 根据邮箱查询管理员
        LambdaQueryWrapper<Administrator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Administrator::getEmail, email);
        Administrator admin = administratorMapper.selectOne(wrapper);
        
        if (admin == null) {
            throw new RuntimeException("管理员不存在");
        }
        
        // 2. 检查状态
        if (!"ACTIVE".equals(admin.getStatus())) {
            throw new RuntimeException("管理员账号已被禁用");
        }
        
        // 3. 验证密码（MD5）
        String encryptedPassword = SecureUtil.md5(password);
        if (!encryptedPassword.equals(admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 4. 返回时密码置空
        admin.setPassword(null);
        return admin;
    }
    
    @Override
    public IPage<Customer> getCustomerList(Page<Customer> page, String level) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(level != null && !level.isEmpty(), Customer::getLevel, level)
                .orderByDesc(Customer::getCreateDate);
        
        IPage<Customer> customerPage = customerMapper.selectPage(page, wrapper);
        
        // 密码置空
        customerPage.getRecords().forEach(customer -> customer.setPassword(null));
        
        return customerPage;
    }
    
    @Override
    public IPage<Merchant> getMerchantList(Page<Merchant> page, String status) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status != null && !status.isEmpty(), Merchant::getStatus, status)
                .orderByDesc(Merchant::getCreateDate);
        
        IPage<Merchant> merchantPage = merchantMapper.selectPage(page, wrapper);
        
        // 密码置空
        merchantPage.getRecords().forEach(merchant -> merchant.setPassword(null));
        
        return merchantPage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Merchant verifyMerchant(Long merchId, String status) {
        // 1. 获取商家信息
        Merchant merchant = merchantMapper.selectById(merchId);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }
        
        // 2. 验证状态
        if (!"ACTIVE".equals(status) && !"REJECTED".equals(status) && !"SUSPENDED".equals(status)) {
            throw new RuntimeException("状态只能是ACTIVE、REJECTED或SUSPENDED");
        }
        
        // 3. 更新状态
        merchant.setStatus(status);
        merchantMapper.updateById(merchant);
        
        // 4. 返回时密码置空
        merchant.setPassword(null);
        return merchant;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Customer updateCustomerLevel(Long userId, String level) {
        // 1. 获取顾客信息
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new RuntimeException("顾客不存在");
        }
        
        // 2. 验证等级
        if (!"NORMAL".equals(level) && !"VIP".equals(level) && !"SVIP".equals(level)) {
            throw new RuntimeException("会员等级只能是NORMAL、VIP或SVIP");
        }
        
        // 3. 更新等级
        customer.setLevel(level);
        customerMapper.updateById(customer);
        
        // 4. 返回时密码置空
        customer.setPassword(null);
        return customer;
    }
    
    @Override
    public java.util.Map<String, Object> getDashboardData() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        // 1. 统计数据
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // 总用户数
        Long totalUsers = customerMapper.selectCount(null);
        stats.put("totalUsers", totalUsers);
        
        // 总商家数
        Long totalMerchants = merchantMapper.selectCount(null);
        stats.put("totalMerchants", totalMerchants);
        
        // 总订单数和总销售额（需要OrderMapper）
        stats.put("totalOrders", 0);
        stats.put("totalSales", "0.00");
        
        result.put("stats", stats);
        
        // 2. 最近注册的用户（前10个）
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Customer> customerWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        customerWrapper.orderByDesc(Customer::getCreateDate).last("LIMIT 10");
        java.util.List<Customer> recentUsers = customerMapper.selectList(customerWrapper);
        // 清除密码
        recentUsers.forEach(c -> c.setPassword(null));
        result.put("recentUsers", recentUsers);
        
        // 3. 最近注册的商家（前10个）
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Merchant> merchantWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        merchantWrapper.orderByDesc(Merchant::getCreateDate).last("LIMIT 10");
        java.util.List<Merchant> recentMerchants = merchantMapper.selectList(merchantWrapper);
        // 清除密码
        recentMerchants.forEach(m -> m.setPassword(null));
        result.put("recentMerchants", recentMerchants);
        
        // 4. 订单趋势（最近7天）- 简化实现，返回模拟数据
        java.util.List<java.util.Map<String, Object>> orderTrend = new java.util.ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            java.util.Map<String, Object> dayData = new java.util.HashMap<>();
            java.time.LocalDate date = today.minusDays(i);
            dayData.put("date", date.format(java.time.format.DateTimeFormatter.ofPattern("MM-dd")));
            dayData.put("count", 0); // 实际应该查询数据库
            orderTrend.add(dayData);
        }
        result.put("orderTrend", orderTrend);
        
        return result;
    }
}
