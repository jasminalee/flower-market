package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.Customer;

/**
 * 顾客服务接口
 */
public interface CustomerService {

    /**
     * 顾客注册
     * @param customer 注册信息
     * @return 注册成功的用户信息
     */
    Customer register(Customer customer);

    /**
     * 顾客登录
     * @param login 登录信息
     * @return 登录成功的用户信息
     */
    Customer login(Customer login);

    /**
     * 根据ID获取顾客信息
     * @param userId 用户ID
     * @return 用户信息
     */
    Customer getCustomerById(Long userId);

    /**
     * 更新顾客信息
     * @param customer 用户信息
     * @return 更新后的用户信息
     */
    Customer updateCustomer(Customer customer);
    
    /**
     * 查询用户余额
     * @param userId 用户ID
     * @return 余额
     */
    java.math.BigDecimal getBalance(Long userId);
    
    /**
     * 余额充值
     * @param userId 用户ID
     * @param amount 充值金额
     * @param paymentMethod 支付方式
     * @return 充值后的余额
     */
    java.math.BigDecimal recharge(Long userId, java.math.BigDecimal amount, String paymentMethod);
    
    /**
     * 查询余额明细
     * @param userId 用户ID
     * @param page 分页参数
     * @return 余额明细列表
     */
    com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<String, Object>> getBalanceHistory(
        Long userId, 
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<String, Object>> page
    );
}
