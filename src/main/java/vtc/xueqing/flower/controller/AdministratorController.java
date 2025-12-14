package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Administrator;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.entity.ProductReview;
import vtc.xueqing.flower.service.AdministratorService;
import vtc.xueqing.flower.service.ProductReviewService;
import vtc.xueqing.flower.vo.ProductReviewVO;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 管理员控制器
 */
@Api(tags = "管理员管理")
@RestController
@RequestMapping("/api/admin")
public class AdministratorController {
    
    @Resource
    private AdministratorService administratorService;
    
    @Resource
    private ProductReviewService productReviewService;
    
    @ApiOperation("管理员登录")
    @PostMapping("/login")
    public Result<Administrator> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");
            
            if (email == null || email.isEmpty()) {
                return Result.error("邮箱不能为空");
            }
            
            if (password == null || password.isEmpty()) {
                return Result.error("密码不能为空");
            }
            
            Administrator admin = administratorService.login(email, password);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取管理后台仪表板数据")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData() {
        try {
            Map<String, Object> data = administratorService.getDashboardData();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取顾客列表（分页）")
    @GetMapping("/customers")
    public Result<IPage<Customer>> getCustomerList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("会员等级") @RequestParam(required = false) String level
    ) {
        try {
            Page<Customer> page = new Page<>(current, size);
            IPage<Customer> customerPage = administratorService.getCustomerList(page, level);
            return Result.success(customerPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取顾客详情")
    @GetMapping("/customers/{id}")
    public Result<Customer> getCustomerById(@PathVariable("id") Long userId) {
        try {
            Customer customer = administratorService.getCustomerById(userId);
            return Result.success(customer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家列表（分页）")
    @GetMapping("/merchants")
    public Result<IPage<Merchant>> getMerchantList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("商家状态") @RequestParam(required = false) String status
    ) {
        try {
            Page<Merchant> page = new Page<>(current, size);
            IPage<Merchant> merchantPage = administratorService.getMerchantList(page, status);
            return Result.success(merchantPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家详情")
    @GetMapping("/merchants/{id}")
    public Result<Merchant> getMerchantById(@PathVariable("id") Long merchId) {
        try {
            Merchant merchant = administratorService.getMerchantById(merchId);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("商家审核")
    @PutMapping({"/merchants/{id}/verify", "/merchants/{id}/audit"})
    public Result<Merchant> verifyMerchant(
            @PathVariable("id") Long merchId,
            @ApiParam("审核状态：ACTIVE-通过，REJECTED-拒绝，SUSPENDED-暂停") @RequestParam String status
    ) {
        try {
            Merchant merchant = administratorService.verifyMerchant(merchId, status);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新顾客会员等级")
    @PutMapping("/customers/{id}/level")
    public Result<Customer> updateCustomerLevel(
            @PathVariable("id") Long userId,
            @ApiParam("会员等级：NORMAL, VIP, SVIP") @RequestParam String level
    ) {
        try {
            Customer customer = administratorService.updateCustomerLevel(userId, level);
            return Result.success(customer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取所有评价列表（管理员）")
    @GetMapping("/reviews")
    public Result<IPage<ProductReviewVO>> getAllReviews(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("审核状态") @RequestParam(required = false) String status
    ) {
        try {
            Page<ProductReviewVO> page = new Page<>(current, size);
            IPage<ProductReviewVO> reviewPage = productReviewService.getAllReviewsWithDetail(page, status);
            return Result.success(reviewPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
