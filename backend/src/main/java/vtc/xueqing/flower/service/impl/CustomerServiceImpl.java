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
 * Customer Service Implementation Class
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Customer register(Customer customer) {
        // 1. Check if email already exists
        LambdaQueryWrapper<Customer> emailWrapper = new LambdaQueryWrapper<>();
        emailWrapper.eq(Customer::getEmail, customer.getEmail());
        if (customerMapper.selectCount(emailWrapper) > 0) {
            throw new BusinessException("This email has already been registered");
        }

        // 2. Check if phone number already exists (if phone number is provided)
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            LambdaQueryWrapper<Customer> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(Customer::getPhone, customer.getPhone());
            if (customerMapper.selectCount(phoneWrapper) > 0) {
                throw new BusinessException("This phone number has already been registered");
            }
        }

        // 3. Encrypt password using MD5
        customer.setPassword(SecureUtil.md5(customer.getPassword()));
        customer.setBalance(BigDecimal.ZERO);
        customer.setLevel(Constants.LEVEL_NORMAL);
        customer.setEmailVerified(0);
        customer.setCreateDate(LocalDateTime.now());
        customer.setUpdateDate(LocalDateTime.now());

        // 4. Save to database
        int result = customerMapper.insert(customer);
        if (result == 0) {
            throw new BusinessException("Registration failed");
        }

        log.info("Customer registration successful, email: {}", customer.getEmail());

        // 5. Return customer object with password cleared
        customer.setPassword(null);
        return customer;
    }

    @Override
    public Customer login(Customer login) {
        // 1. Query user by email or phone number
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(Customer::getEmail, login.getEmail())
                .or()
                .eq(Customer::getPhone, login.getEmail()));

        Customer customer = customerMapper.selectOne(wrapper);
        if (customer == null) {
            throw new BusinessException("Account does not exist");
        }

        // 2. Verify password (compare after MD5 encryption)
        String encryptedPassword = SecureUtil.md5(login.getPassword());
        log.info("Password verification - Input password: {}, After MD5: {}, Database password: {}", login.getPassword(), encryptedPassword, customer.getPassword());
        if (!encryptedPassword.equals(customer.getPassword())) {
            throw new BusinessException("Incorrect password");
        }

        log.info("Customer login successful, ID: {}, Email: {}", customer.getUserId(), customer.getEmail());

        // 3. Return customer object with password cleared
        customer.setPassword(null);
        return customer;
    }

    @Override
    public Customer getCustomerById(Long userId) {
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new BusinessException("User does not exist");
        }
        // Clear password
        customer.setPassword(null);
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Customer updateCustomer(Customer customer) {
        // 1. Check if user exists
        Customer existing = customerMapper.selectById(customer.getUserId());
        if (existing == null) {
            throw new BusinessException("User does not exist");
        }
        
        // 2. If updating password, need to encrypt
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            customer.setPassword(SecureUtil.md5(customer.getPassword()));
        } else {
            // Do not update password
            customer.setPassword(null);
        }
        
        // 3. Update
        customer.setUpdateDate(LocalDateTime.now());
        customerMapper.updateById(customer);
        
        // 4. Return updated information (password cleared)
        Customer updated = customerMapper.selectById(customer.getUserId());
        updated.setPassword(null);
        return updated;
    }
    
    @Override
    public BigDecimal getBalance(Long userId) {
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new BusinessException("User does not exist");
        }
        return customer.getBalance();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal recharge(Long userId, BigDecimal amount, String paymentMethod) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Recharge amount must be greater than 0");
        }
        
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new BusinessException("User does not exist");
        }
        
        // Update balance
        BigDecimal newBalance = customer.getBalance().add(amount);
        customer.setBalance(newBalance);
        customer.setUpdateDate(LocalDateTime.now());
        customerMapper.updateById(customer);
        
        log.info("User {} recharge successful, recharge amount: {}, current balance: {}", userId, amount, newBalance);
        
        return newBalance;
    }
    
    @Override
    public com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<String, Object>> getBalanceHistory(
            Long userId, 
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<String, Object>> page) {
        
        // TODO: In actual projects, there should be a dedicated balance change record table
        // Return mock data here
        java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();
        
        // Simulate some balance change records
        Customer customer = customerMapper.selectById(userId);
        if (customer != null) {
            java.util.Map<String, Object> record = new java.util.HashMap<>();
            record.put("createDate", LocalDateTime.now().toString());
            record.put("type", "RECHARGE");
            record.put("amount", 100.00);
            record.put("description", "Balance recharge");
            record.put("balance", customer.getBalance());
            records.add(record);
        }
        
        page.setRecords(records);
        page.setTotal(records.size());
        
        return page;
    }
}
