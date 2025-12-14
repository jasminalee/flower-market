package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Administrator;
import vtc.xueqing.flower.entity.CareKnowledge;
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
    
    @ApiOperation("获取所有订单列表（管理员）")
    @GetMapping("/orders")
    public Result<IPage<vtc.xueqing.flower.vo.OrderVO>> getAllOrders(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("订单状态") @RequestParam(required = false) String status,
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword
    ) {
        try {
            Page<vtc.xueqing.flower.vo.OrderVO> page = new Page<>(current, size);
            IPage<vtc.xueqing.flower.vo.OrderVO> orderPage = administratorService.getAllOrders(page, status, keyword);
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取养护知识列表（管理员）")
    @GetMapping("/knowledge")
    public Result<IPage<vtc.xueqing.flower.entity.CareKnowledge>> getKnowledgeList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("搜索关键词") @RequestParam(required = false) String keyword,
            @ApiParam("分类") @RequestParam(required = false) String category,
            @ApiParam("状态") @RequestParam(required = false) String status
    ) {
        try {
            Page<vtc.xueqing.flower.entity.CareKnowledge> page = new Page<>(current, size);
            IPage<vtc.xueqing.flower.entity.CareKnowledge> knowledgePage = administratorService.getKnowledgeList(page, keyword, category, status);
            return Result.success(knowledgePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取养护知识详情（管理员）")
    @GetMapping("/knowledge/{id}")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> getKnowledgeById(@PathVariable("id") Long id) {
        try {
            vtc.xueqing.flower.entity.CareKnowledge knowledge = administratorService.getKnowledgeById(id);
            return Result.success(knowledge);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("创建养护知识（管理员）")
    @PostMapping("/knowledge")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> createKnowledge(@RequestBody vtc.xueqing.flower.entity.CareKnowledge knowledge) {
        try {
            vtc.xueqing.flower.entity.CareKnowledge created = administratorService.createKnowledge(knowledge);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新养护知识（管理员）")
    @PutMapping("/knowledge/{id}")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> updateKnowledge(
            @PathVariable("id") Long id,
            @RequestBody vtc.xueqing.flower.entity.CareKnowledge knowledge
    ) {
        try {
            knowledge.setId(id);
            vtc.xueqing.flower.entity.CareKnowledge updated = administratorService.updateKnowledge(knowledge);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新养护知识状态（管理员）")
    @PutMapping("/knowledge/{id}/status")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> updateKnowledgeStatus(
            @PathVariable("id") Long id,
            @ApiParam("状态：PUBLISHED-已发布，DRAFT-草稿") @RequestParam String status
    ) {
        try {
            vtc.xueqing.flower.entity.CareKnowledge updated = administratorService.updateKnowledgeStatus(id, status);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除养护知识（管理员）")
    @DeleteMapping("/knowledge/{id}")
    public Result<Void> deleteKnowledge(@PathVariable("id") Long id) {
        try {
            administratorService.deleteKnowledge(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取管理员个人信息")
    @GetMapping("/profile")
    public Result<Administrator> getProfile(@RequestParam Long adminId) {
        try {
            Administrator admin = administratorService.getProfileById(adminId);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新管理员个人信息")
    @PutMapping("/profile")
    public Result<Administrator> updateProfile(@RequestBody Map<String, Object> profileData) {
        try {
            Long adminId = Long.valueOf(profileData.get("adminId").toString());
            String name = (String) profileData.get("name");
            
            Administrator admin = administratorService.updateProfile(adminId, name);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("修改管理员密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> passwordData) {
        try {
            Long adminId = Long.valueOf(passwordData.get("adminId"));
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (oldPassword == null || oldPassword.isEmpty()) {
                return Result.error("原密码不能为空");
            }
            
            if (newPassword == null || newPassword.isEmpty()) {
                return Result.error("新密码不能为空");
            }
            
            if (newPassword.length() < 6) {
                return Result.error("新密码长度不能少于6位");
            }
            
            administratorService.updatePassword(adminId, oldPassword, newPassword);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
