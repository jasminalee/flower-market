package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Merchant;
import vtc.xueqing.flower.service.MerchantService;

import javax.annotation.Resource;

/**
 * 商家Controller
 */
@Slf4j
@Api(tags = "商家管理接口")
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {

    @Resource
    private MerchantService merchantService;

    @ApiOperation("商家注册")
    @PostMapping("/register")
    public Result<Merchant> register(@Validated @RequestBody Merchant merchant) {
        Merchant result = merchantService.register(merchant);
        return Result.success("注册成功，请等待管理员审核", result);
    }

    @ApiOperation("商家登录")
    @PostMapping("/login")
    public Result<Merchant> login(@Validated @RequestBody Merchant login) {
        Merchant merchant = merchantService.login(login);
        return Result.success("登录成功", merchant);
    }

    @ApiOperation("获取商家信息")
    @GetMapping("/profile/{merchId}")
    public Result<Merchant> getProfile(@PathVariable Long merchId) {
        Merchant merchant = merchantService.getMerchantById(merchId);
        return Result.success(merchant);
    }
    
    @ApiOperation("更新商家信息")
    @PutMapping("/profile")
    public Result<Merchant> updateProfile(@RequestBody Merchant merchant) {
        try {
            Merchant updated = merchantService.updateMerchant(merchant);
            return Result.success("更新成功", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家工作台数据")
    @GetMapping("/dashboard")
    public Result<java.util.Map<String, Object>> getDashboardData(@RequestParam Long merchId) {
        try {
            java.util.Map<String, Object> data = merchantService.getDashboardData(merchId);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家商品列表")
    @GetMapping("/products")
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product>> getMerchantProducts(
            @RequestParam(required = false) Long merchId,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long catId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        try {
            // 如果没有传递merchId，返回错误提示
            if (merchId == null) {
                return Result.error("商家ID不能为空");
            }
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> page;
            
            // 如果有name、catId、status等筛选条件，使用详细筛选方法
            if (name != null || catId != null || status != null) {
                page = ((vtc.xueqing.flower.service.impl.MerchantServiceImpl) merchantService)
                    .getMerchantProductsWithFilter(merchId, current, size, name, catId, status);
            } else {
                // 否则使用简单的关键词搜索
                page = merchantService.getMerchantProducts(merchId, current, size, keyword);
            }
            
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家订单列表")
    @GetMapping("/orders")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Order>> getMerchantOrders(
            @RequestParam(required = false) Long merchId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status
    ) {
        try {
            // 如果没有传递merchId，返回错误提示
            if (merchId == null) {
                return Result.error("商家ID不能为空");
            }
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Order> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
            com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Order> orderPage = 
                merchantService.getMerchantOrders(page, merchId, status);
            
            return Result.success(orderPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家优惠券列表")
    @GetMapping("/coupons")
    public Result<com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon>> getMerchantCoupons(
            @RequestParam(required = false) Long merchId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status
    ) {
        try {
            if (merchId == null) {
                return Result.error("商家ID不能为空");
            }
            
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Coupon> page = 
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
            com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon> couponPage = 
                merchantService.getMerchantCoupons(page, merchId, status);
            
            return Result.success(couponPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取商家优惠券详情")
    @GetMapping("/coupons/{id}")
    public Result<vtc.xueqing.flower.entity.Coupon> getMerchantCoupon(@PathVariable Long id) {
        try {
            vtc.xueqing.flower.entity.Coupon coupon = merchantService.getMerchantCouponById(id);
            if (coupon == null) {
                return Result.error("优惠券不存在");
            }
            return Result.success(coupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("创建商家优惠券")
    @PostMapping("/coupons")
    public Result<vtc.xueqing.flower.entity.Coupon> createMerchantCoupon(@RequestBody vtc.xueqing.flower.entity.Coupon coupon) {
        try {
            vtc.xueqing.flower.entity.Coupon created = merchantService.createMerchantCoupon(coupon);
            return Result.success("创建成功", created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新商家优惠券")
    @PutMapping("/coupons/{id}")
    public Result<vtc.xueqing.flower.entity.Coupon> updateMerchantCoupon(
            @PathVariable Long id,
            @RequestBody vtc.xueqing.flower.entity.Coupon coupon) {
        try {
            coupon.setCouponId(id);
            vtc.xueqing.flower.entity.Coupon updated = merchantService.updateMerchantCoupon(coupon);
            return Result.success("更新成功", updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除商家优惠券")
    @DeleteMapping("/coupons/{id}")
    public Result<Void> deleteMerchantCoupon(@PathVariable Long id) {
        try {
            merchantService.deleteMerchantCoupon(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
