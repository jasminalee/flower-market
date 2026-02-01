package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.Customer;

/**
 * Customer service interface.
 */
public interface CustomerService {

    /**
     * Customer registration.
     * @param customer registration info
     * @return registered customer info
     */
    Customer register(Customer customer);

    /**
     * Customer login.
     * @param login login info
     * @return customer info after login
     */
    Customer login(Customer login);

    /**
     * Get customer by ID.
     * @param userId user ID
     * @return customer info
     */
    Customer getCustomerById(Long userId);

    /**
     * Update customer info.
     * @param customer customer info
     * @return updated customer info
     */
    Customer updateCustomer(Customer customer);
    
    /**
     * Query user balance.
     * @param userId user ID
     * @return balance
     */
    java.math.BigDecimal getBalance(Long userId);
    
    /**
     * Balance recharge.
     * @param userId user ID
     * @param amount recharge amount
     * @param paymentMethod payment method
     * @return balance after recharge
     */
    java.math.BigDecimal recharge(Long userId, java.math.BigDecimal amount, String paymentMethod);
    
    /**
     * Query balance history.
     * @param userId user ID
     * @param page page params
     * @return balance history list
     */
    com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<String, Object>> getBalanceHistory(
        Long userId, 
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<String, Object>> page
    );
}
