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
}
