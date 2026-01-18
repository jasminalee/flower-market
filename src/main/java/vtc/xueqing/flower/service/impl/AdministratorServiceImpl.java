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
import vtc.xueqing.flower.entity.CareKnowledge;
import vtc.xueqing.flower.mapper.AdministratorMapper;
import vtc.xueqing.flower.mapper.CareKnowledgeMapper;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.mapper.MerchantMapper;
import vtc.xueqing.flower.mapper.OrderMapper;
import vtc.xueqing.flower.service.AdministratorService;
import vtc.xueqing.flower.vo.OrderVO;

import javax.annotation.Resource;

/**
 * Administrator Service Implementation Class
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {
    
    @Resource
    private AdministratorMapper administratorMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Resource
    private MerchantMapper merchantMapper;
    
    @Resource
    private OrderMapper orderMapper;
    
    @Resource
    private CareKnowledgeMapper careKnowledgeMapper;
    
    @Override
    public Administrator login(String email, String password) {
        // 1. Query administrator by email
        LambdaQueryWrapper<Administrator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Administrator::getEmail, email);
        Administrator admin = administratorMapper.selectOne(wrapper);
        
        if (admin == null) {
            throw new RuntimeException("Administrator does not exist");
        }
        
        // 2. Check status
        if (!"ACTIVE".equals(admin.getStatus())) {
            throw new RuntimeException("Administrator account has been disabled");
        }
        
        // 3. Verify password (MD5)
        String encryptedPassword = SecureUtil.md5(password);
        if (!encryptedPassword.equals(admin.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }
        
        // 4. Set password to null when returning
        admin.setPassword(null);
        return admin;
    }
    
    @Override
    public IPage<Customer> getCustomerList(Page<Customer> page, String level) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(level != null && !level.isEmpty(), Customer::getLevel, level)
                .orderByDesc(Customer::getCreateDate);
        
        IPage<Customer> customerPage = customerMapper.selectPage(page, wrapper);
        
        // Set password to null
        customerPage.getRecords().forEach(customer -> customer.setPassword(null));
        
        return customerPage;
    }
    
    @Override
    public Customer getCustomerById(Long userId) {
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new RuntimeException("User does not exist");
        }
        // Set password to null
        customer.setPassword(null);
        return customer;
    }
    
    @Override
    public Merchant getMerchantById(Long merchId) {
        Merchant merchant = merchantMapper.selectById(merchId);
        if (merchant == null) {
            throw new RuntimeException("Merchant does not exist");
        }
        // Set password to null
        merchant.setPassword(null);
        return merchant;
    }
    
    @Override
    public IPage<Merchant> getMerchantList(Page<Merchant> page, String status) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status != null && !status.isEmpty(), Merchant::getStatus, status)
                .orderByDesc(Merchant::getCreateDate);
        
        IPage<Merchant> merchantPage = merchantMapper.selectPage(page, wrapper);
        
        // Set password to null
        merchantPage.getRecords().forEach(merchant -> merchant.setPassword(null));
        
        return merchantPage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Merchant verifyMerchant(Long merchId, String status) {
        // 1. Get merchant information
        Merchant merchant = merchantMapper.selectById(merchId);
        if (merchant == null) {
            throw new RuntimeException("Merchant does not exist");
        }
        
        // 2. Validate status
        if (!"ACTIVE".equals(status) && !"REJECTED".equals(status) && !"SUSPENDED".equals(status)) {
            throw new RuntimeException("Status can only be ACTIVE, REJECTED, or SUSPENDED");
        }
        
        // 3. Update status
        merchant.setStatus(status);
        merchantMapper.updateById(merchant);
        
        // 4. Set password to null when returning
        merchant.setPassword(null);
        return merchant;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Customer updateCustomerLevel(Long userId, String level) {
        // 1. Get customer information
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new RuntimeException("Customer does not exist");
        }
        
        // 2. Validate level
        if (!"NORMAL".equals(level) && !"VIP".equals(level) && !"SVIP".equals(level)) {
            throw new RuntimeException("Membership level can only be NORMAL, VIP, or SVIP");
        }
        
        // 3. Update level
        customer.setLevel(level);
        customerMapper.updateById(customer);
        
        // 4. Set password to null when returning
        customer.setPassword(null);
        return customer;
    }
    
    @Override
    public java.util.Map<String, Object> getDashboardData() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        // 1. Statistics data
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        // Total users
        Long totalUsers = customerMapper.selectCount(null);
        stats.put("totalUsers", totalUsers);
        
        // Total merchants
        Long totalMerchants = merchantMapper.selectCount(null);
        stats.put("totalMerchants", totalMerchants);
        
        // Total order count and total sales (requires OrderMapper)
        stats.put("totalOrders", 0);
        stats.put("totalSales", "0.00");
        
        result.put("stats", stats);
        
        // 2. Recently registered users (top 10)
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Customer> customerWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        customerWrapper.orderByDesc(Customer::getCreateDate).last("LIMIT 10");
        java.util.List<Customer> recentUsers = customerMapper.selectList(customerWrapper);
        // Clear password
        recentUsers.forEach(c -> c.setPassword(null));
        result.put("recentUsers", recentUsers);
        
        // 3. Recently registered merchants (top 10)
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Merchant> merchantWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        merchantWrapper.orderByDesc(Merchant::getCreateDate).last("LIMIT 10");
        java.util.List<Merchant> recentMerchants = merchantMapper.selectList(merchantWrapper);
        // Clear password
        recentMerchants.forEach(m -> m.setPassword(null));
        result.put("recentMerchants", recentMerchants);
        
        // 4. Order trend (last 7 days) - Simplified implementation, return mock data
        java.util.List<java.util.Map<String, Object>> orderTrend = new java.util.ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            java.util.Map<String, Object> dayData = new java.util.HashMap<>();
            java.time.LocalDate date = today.minusDays(i);
            dayData.put("date", date.format(java.time.format.DateTimeFormatter.ofPattern("MM-dd")));
            dayData.put("count", 0); // Actually should query database
            orderTrend.add(dayData);
        }
        result.put("orderTrend", orderTrend);
        
        return result;
    }
    
    @Override
    public IPage<OrderVO> getAllOrders(Page<OrderVO> page, String status, String keyword) {
        return orderMapper.selectAllOrdersWithDetail(page, status, keyword);
    }
    
    @Override
    public IPage<CareKnowledge> getKnowledgeList(Page<CareKnowledge> page, String keyword, String category, String status) {
        LambdaQueryWrapper<CareKnowledge> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(keyword != null && !keyword.isEmpty(), CareKnowledge::getTitle, keyword)
                .eq(category != null && !category.isEmpty(), CareKnowledge::getCategory, category)
                .eq(status != null && !status.isEmpty(), CareKnowledge::getStatus, status)
                .orderByDesc(CareKnowledge::getCreateDate);
        return careKnowledgeMapper.selectPage(page, wrapper);
    }
    
    @Override
    public CareKnowledge getKnowledgeById(Long id) {
        CareKnowledge knowledge = careKnowledgeMapper.selectById(id);
        if (knowledge == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        return knowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge createKnowledge(CareKnowledge knowledge) {
        // Set default status
        if (knowledge.getStatus() == null || knowledge.getStatus().isEmpty()) {
            knowledge.setStatus("PUBLISHED");
        }
        
        // Initialize view count
        if (knowledge.getViewCount() == null) {
            knowledge.setViewCount(0);
        }
        
        // Validate required fields
        if (knowledge.getTitle() == null || knowledge.getTitle().isEmpty()) {
            throw new RuntimeException("Title cannot be empty");
        }
        
        if (knowledge.getContent() == null || knowledge.getContent().isEmpty()) {
            throw new RuntimeException("Content cannot be empty");
        }
        
        careKnowledgeMapper.insert(knowledge);
        return knowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge updateKnowledge(CareKnowledge knowledge) {
        CareKnowledge existing = careKnowledgeMapper.selectById(knowledge.getId());
        if (existing == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        
        careKnowledgeMapper.updateById(knowledge);
        return careKnowledgeMapper.selectById(knowledge.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge updateKnowledgeStatus(Long id, String status) {
        CareKnowledge knowledge = careKnowledgeMapper.selectById(id);
        if (knowledge == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        
        // Validate status value
        if (!"PUBLISHED".equals(status) && !"DRAFT".equals(status)) {
            throw new RuntimeException("Status can only be PUBLISHED or DRAFT");
        }
        
        knowledge.setStatus(status);
        careKnowledgeMapper.updateById(knowledge);
        return knowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteKnowledge(Long id) {
        CareKnowledge knowledge = careKnowledgeMapper.selectById(id);
        if (knowledge == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        
        careKnowledgeMapper.deleteById(id);
    }
    
    @Override
    public Administrator getProfileById(Long adminId) {
        Administrator admin = administratorMapper.selectById(adminId);
        if (admin == null) {
            throw new RuntimeException("Administrator does not exist");
        }
        // Set password to null
        admin.setPassword(null);
        return admin;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Administrator updateProfile(Long adminId, String name) {
        Administrator admin = administratorMapper.selectById(adminId);
        if (admin == null) {
            throw new RuntimeException("Administrator does not exist");
        }
        
        if (name != null && !name.trim().isEmpty()) {
            admin.setName(name);
        }
        
        administratorMapper.updateById(admin);
        // Set password to null
        admin.setPassword(null);
        return admin;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long adminId, String oldPassword, String newPassword) {
        Administrator admin = administratorMapper.selectById(adminId);
        if (admin == null) {
            throw new RuntimeException("Administrator does not exist");
        }
        
        // Verify original password
        String encryptedOldPassword = SecureUtil.md5(oldPassword);
        if (!encryptedOldPassword.equals(admin.getPassword())) {
            throw new RuntimeException("Original password is incorrect");
        }
        
        // Set new password
        String encryptedNewPassword = SecureUtil.md5(newPassword);
        admin.setPassword(encryptedNewPassword);
        administratorMapper.updateById(admin);
    }
}
