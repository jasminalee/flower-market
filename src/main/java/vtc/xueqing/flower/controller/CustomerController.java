package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.service.CustomerService;

import javax.annotation.Resource;

/**
 * 顾客Controller
 */
@Slf4j
@Api(tags = "顾客管理接口")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @ApiOperation("顾客注册")
    @PostMapping("/register")
    public Result<Customer> register(@Validated @RequestBody Customer customer) {
        Customer result = customerService.register(customer);
        return Result.success("注册成功", result);
    }

    @ApiOperation("顾客登录")
    @PostMapping("/login")
    public Result<Customer> login(@Validated @RequestBody Customer login) {
        Customer customer = customerService.login(login);
        return Result.success("登录成功", customer);
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/profile/{userId}")
    public Result<Customer> getProfile(@PathVariable Long userId) {
        Customer customer = customerService.getCustomerById(userId);
        return Result.success(customer);
    }
    
    @ApiOperation("更新个人信息")
    @PutMapping("/profile")
    public Result<Customer> updateProfile(@RequestBody Customer customer) {
        try {
            Customer updated = customerService.updateCustomer(customer);
            return Result.success("更新成功", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("查询余额")
    @GetMapping("/balance")
    public Result<java.math.BigDecimal> getBalance(@RequestParam Long userId) {
        try {
            java.math.BigDecimal balance = customerService.getBalance(userId);
            return Result.success(balance);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("余额充值")
    @PostMapping("/balance/recharge")
    public Result<java.util.Map<String, Object>> recharge(@RequestBody java.util.Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            java.math.BigDecimal amount = new java.math.BigDecimal(params.get("amount").toString());
            String paymentMethod = params.get("paymentMethod").toString();
            
            java.math.BigDecimal newBalance = customerService.recharge(userId, amount, paymentMethod);
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("balance", newBalance);
            
            return Result.success("充值成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("查询余额明细")
    @GetMapping("/balance/history")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<String, Object>>> getBalanceHistory(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        try {
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.Map<String, Object>> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
            com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<String, Object>> history = 
                customerService.getBalanceHistory(userId, page);
            return Result.success(history);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
