package vtc.xueqing.flower.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.exception.BusinessException;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.service.CustomerService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 顾客服务实现类
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Customer register(Customer customer) {
        // 1. 检查邮箱是否已存在
        LambdaQueryWrapper<Customer> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(Customer::getEmail, customer.getEmail());
        if (customerMapper.selectCount(emailWrapper) > 0) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 2. 检查手机号是否已存在（如果提供了手机号）
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            LambdaQueryWrapper<Customer> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(Customer::getPhone, customer.getPhone());
            if (customerMapper.selectCount(phoneWrapper) > 0) {
                throw new BusinessException("该手机号已被注册");
            }
        }

        // 3. 使用MD5加密密码
        customer.setPassword(SecureUtil.md5(customer.getPassword()));
        customer.setBalance(BigDecimal.ZERO);
        customer.setLevel(Constants.LEVEL_NORMAL);
        customer.setEmailVerified(0);
        customer.setCreateDate(LocalDateTime.now());
        customer.setUpdateDate(LocalDateTime.now());

        // 4. 保存到数据库
        int result = customerMapper.insert(customer);
        if (result == 0) {
            throw new BusinessException("注册失败");
        }

        log.info("顾客注册成功，邮箱：{}", customer.getEmail());

        // 5. 返回密码置空的customer对象
        customer.setPassword(null);
        return customer;
    }

    @Override
    public Customer login(Customer login) {
        // 1. 根据邮箱或手机号查询用户
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Customer::getEmail, login.getEmail())
                .or()
                .eq(Customer::getPhone, login.getEmail()));

        Customer customer = customerMapper.selectOne(wrapper);
        if (customer == null) {
            throw new BusinessException("账号不存在");
        }

        // 2. 验证密码（MD5加密后比较）
        String encryptedPassword = SecureUtil.md5(login.getPassword());
        log.info("密码验证 - 输入密码: {}, MD5加密后: {}, 数据库密码: {}", login.getPassword(), encryptedPassword, customer.getPassword());
        if (!encryptedPassword.equals(customer.getPassword())) {
            throw new BusinessException("密码错误");
        }

        log.info("顾客登录成功，ID：{}, 邮箱：{}", customer.getUserId(), customer.getEmail());

        // 3. 返回密码置空的customer对象
        customer.setPassword(null);
        return customer;
    }

    @Override
    public Customer getCustomerById(Long userId) {
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new BusinessException("用户不存在");
        }
        // 密码置空
        customer.setPassword(null);
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Customer updateCustomer(Customer customer) {
        // 1. 检查用户是否存在
        Customer existing = customerMapper.selectById(customer.getUserId());
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 如果更新密码，需要加密
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            customer.setPassword(SecureUtil.md5(customer.getPassword()));
        } else {
            // 不更新密码
            customer.setPassword(null);
        }
        
        // 3. 更新
        customer.setUpdateDate(LocalDateTime.now());
        customerMapper.updateById(customer);
        
        // 4. 返回更新后的信息（密码置空）
        Customer updated = customerMapper.selectById(customer.getUserId());
        updated.setPassword(null);
        return updated;
    }
    
    @Override
    public BigDecimal getBalance(Long userId) {
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new BusinessException("用户不存在");
        }
        return customer.getBalance();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal recharge(Long userId, BigDecimal amount, String paymentMethod) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 更新余额
        BigDecimal newBalance = customer.getBalance().add(amount);
        customer.setBalance(newBalance);
        customer.setUpdateDate(LocalDateTime.now());
        customerMapper.updateById(customer);
        
        log.info("用户{}充值成功，充值金额：{}，当前余额：{}", userId, amount, newBalance);
        
        return newBalance;
    }
    
    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<String, Object>> getBalanceHistory(
            Long userId, 
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<String, Object>> page) {
        
        // TODO: 实际项目中应该有专门的余额变动记录表
        // 这里返回模拟数据
        java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();
        
        // 模拟一些余额变动记录
        Customer customer = customerMapper.selectById(userId);
        if (customer != null) {
            java.util.Map<String, Object> record = new java.util.HashMap<>();
            record.put("createDate", LocalDateTime.now().toString());
            record.put("type", "RECHARGE");
            record.put("amount", 100.00);
            record.put("description", "余额充值");
            record.put("balance", customer.getBalance());
            records.add(record);
        }
        
        page.setRecords(records);
        page.setTotal(records.size());
        
        return page;
    }
}
