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
 * Customer Controller
 */
@Slf4j
@Api(tags = "Customer Management Interface")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @ApiOperation("Customer Registration")
    @PostMapping("/register")
    public Result<Customer> register(@Validated @RequestBody Customer customer) {
        Customer result = customerService.register(customer);
        return Result.success("Registration Successful", result);
    }

    @ApiOperation("Customer Login")
    @PostMapping("/login")
    public Result<Customer> login(@Validated @RequestBody Customer login) {
        Customer customer = customerService.login(login);
        return Result.success("Login Successful", customer);
    }

    @ApiOperation("Get Personal Profile")
    @GetMapping("/profile/{userId}")
    public Result<Customer> getProfile(@PathVariable Long userId) {
        Customer customer = customerService.getCustomerById(userId);
        return Result.success(customer);
    }
    
    @ApiOperation("Update Personal Profile")
    @PutMapping("/profile")
    public Result<Customer> updateProfile(@RequestBody Customer customer) {
        try {
            Customer updated = customerService.updateCustomer(customer);
            return Result.success("Update Successful", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Query Balance")
    @GetMapping("/balance")
    public Result<java.math.BigDecimal> getBalance(@RequestParam Long userId) {
        try {
            java.math.BigDecimal balance = customerService.getBalance(userId);
            return Result.success(balance);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Balance Recharge")
    @PostMapping("/balance/recharge")
    public Result<java.util.Map<String, Object>> recharge(@RequestBody java.util.Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            java.math.BigDecimal amount = new java.math.BigDecimal(params.get("amount").toString());
            String paymentMethod = params.get("paymentMethod").toString();
            
            java.math.BigDecimal newBalance = customerService.recharge(userId, amount, paymentMethod);
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("balance", newBalance);
            
            return Result.success("Recharge Successful", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Query Balance Details")
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
