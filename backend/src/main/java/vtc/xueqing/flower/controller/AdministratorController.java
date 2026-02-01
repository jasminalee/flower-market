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
 * Administrator Controller
 */
@Api(tags = "Administrator Management")
@RestController
@RequestMapping("/api/admin")
public class AdministratorController {
    
    @Resource
    private AdministratorService administratorService;
    
    @Resource
    private ProductReviewService productReviewService;
    
    @ApiOperation("Administrator Login")
    @PostMapping("/login")
    public Result<Administrator> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");
            
            if (email == null || email.isEmpty()) {
                return Result.error("Email cannot be empty");
            }
            
            if (password == null || password.isEmpty()) {
                return Result.error("Password cannot be empty");
            }
            
            Administrator admin = administratorService.login(email, password);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Admin Dashboard Data")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData() {
        try {
            Map<String, Object> data = administratorService.getDashboardData();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Customer List (Pagination)")
    @GetMapping("/customers")
    public Result<IPage<Customer>> getCustomerList(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Membership Level") @RequestParam(required = false) String level
    ) {
        try {
            Page<Customer> page = new Page<>(current, size);
            IPage<Customer> customerPage = administratorService.getCustomerList(page, level);
            return Result.success(customerPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Customer Details")
    @GetMapping("/customers/{id}")
    public Result<Customer> getCustomerById(@PathVariable("id") Long userId) {
        try {
            Customer customer = administratorService.getCustomerById(userId);
            return Result.success(customer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant List (Pagination)")
    @GetMapping("/merchants")
    public Result<IPage<Merchant>> getMerchantList(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Merchant Status") @RequestParam(required = false) String status
    ) {
        try {
            Page<Merchant> page = new Page<>(current, size);
            IPage<Merchant> merchantPage = administratorService.getMerchantList(page, status);
            return Result.success(merchantPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Merchant Details")
    @GetMapping("/merchants/{id}")
    public Result<Merchant> getMerchantById(@PathVariable("id") Long merchId) {
        try {
            Merchant merchant = administratorService.getMerchantById(merchId);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Merchant Verification")
    @PutMapping({"/merchants/{id}/verify", "/merchants/{id}/audit"})
    public Result<Merchant> verifyMerchant(
            @PathVariable("id") Long merchId,
            @ApiParam("Verification Status: ACTIVE-approved, REJECTED-rejected, SUSPENDED-suspended") @RequestParam String status
    ) {
        try {
            Merchant merchant = administratorService.verifyMerchant(merchId, status);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update Customer Membership Level")
    @PutMapping("/customers/{id}/level")
    public Result<Customer> updateCustomerLevel(
            @PathVariable("id") Long userId,
            @ApiParam("Membership Level: NORMAL, VIP, SVIP") @RequestParam String level
    ) {
        try {
            Customer customer = administratorService.updateCustomerLevel(userId, level);
            return Result.success(customer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get All Reviews List (Admin)")
    @GetMapping("/reviews")
    public Result<IPage<ProductReviewVO>> getAllReviews(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Review Status") @RequestParam(required = false) String status
    ) {
        try {
            Page<ProductReviewVO> page = new Page<>(current, size);
            IPage<ProductReviewVO> reviewPage = productReviewService.getAllReviewsWithDetail(page, status);
            return Result.success(reviewPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get All Orders List (Admin)")
    @GetMapping("/orders")
    public Result<IPage<vtc.xueqing.flower.vo.OrderVO>> getAllOrders(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Order Status") @RequestParam(required = false) String status,
            @ApiParam("Search Keyword") @RequestParam(required = false) String keyword
    ) {
        try {
            Page<vtc.xueqing.flower.vo.OrderVO> page = new Page<>(current, size);
            IPage<vtc.xueqing.flower.vo.OrderVO> orderPage = administratorService.getAllOrders(page, status, keyword);
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Care Knowledge List (Admin)")
    @GetMapping("/knowledge")
    public Result<IPage<vtc.xueqing.flower.entity.CareKnowledge>> getKnowledgeList(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Search Keywords") @RequestParam(required = false) String keyword,
            @ApiParam("Category") @RequestParam(required = false) String category,
            @ApiParam("Status") @RequestParam(required = false) String status
    ) {
        try {
            Page<vtc.xueqing.flower.entity.CareKnowledge> page = new Page<>(current, size);
            IPage<vtc.xueqing.flower.entity.CareKnowledge> knowledgePage = administratorService.getKnowledgeList(page, keyword, category, status);
            return Result.success(knowledgePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Care Knowledge Details (Admin)")
    @GetMapping("/knowledge/{id}")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> getKnowledgeById(@PathVariable("id") Long id) {
        try {
            vtc.xueqing.flower.entity.CareKnowledge knowledge = administratorService.getKnowledgeById(id);
            return Result.success(knowledge);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Create Care Knowledge (Admin)")
    @PostMapping("/knowledge")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> createKnowledge(@RequestBody vtc.xueqing.flower.entity.CareKnowledge knowledge) {
        try {
            vtc.xueqing.flower.entity.CareKnowledge created = administratorService.createKnowledge(knowledge);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update Care Knowledge (Admin)")
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
    
    @ApiOperation("Update Care Knowledge Status (Admin)")
    @PutMapping("/knowledge/{id}/status")
    public Result<vtc.xueqing.flower.entity.CareKnowledge> updateKnowledgeStatus(
            @PathVariable("id") Long id,
            @ApiParam("Status: PUBLISHED-published, DRAFT-draft") @RequestParam String status
    ) {
        try {
            vtc.xueqing.flower.entity.CareKnowledge updated = administratorService.updateKnowledgeStatus(id, status);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Delete Care Knowledge (Admin)")
    @DeleteMapping("/knowledge/{id}")
    public Result<Void> deleteKnowledge(@PathVariable("id") Long id) {
        try {
            administratorService.deleteKnowledge(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Administrator Profile")
    @GetMapping("/profile")
    public Result<Administrator> getProfile(@RequestParam Long adminId) {
        try {
            Administrator admin = administratorService.getProfileById(adminId);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update Administrator Profile")
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
    
    @ApiOperation("Change Administrator Password")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> passwordData) {
        try {
            Long adminId = Long.valueOf(passwordData.get("adminId"));
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (oldPassword == null || oldPassword.isEmpty()) {
                return Result.error("Old password cannot be empty");
            }
            
            if (newPassword == null || newPassword.isEmpty()) {
                return Result.error("New password cannot be empty");
            }
            
            if (newPassword.length() < 6) {
                return Result.error("New password length cannot be less than 6 characters");
            }
            
            administratorService.updatePassword(adminId, oldPassword, newPassword);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
