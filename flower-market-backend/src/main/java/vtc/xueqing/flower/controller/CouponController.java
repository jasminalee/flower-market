package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Coupon;
import vtc.xueqing.flower.entity.CustomerCoupon;
import vtc.xueqing.flower.service.CouponService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Coupon Controller
 */
@Api(tags = "Coupon Management")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    
    @Resource
    private CouponService couponService;
    
    @ApiOperation("Get Available Coupons List (Pagination)")
    @GetMapping
    public Result<IPage<Coupon>> getAvailableCoupons(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Merchant ID") @RequestParam(required = false) Long merchId
    ) {
        try {
            Page<Coupon> page = new Page<>(current, size);
            IPage<Coupon> couponPage = couponService.getAvailableCoupons(page, merchId);
            return Result.success(couponPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("User Receives Coupon")
    @PostMapping("/{id}/receive")
    public Result<CustomerCoupon> receiveCoupon(
            @PathVariable("id") Long id,
            @ApiParam("User ID") @RequestParam Long userId
    ) {
        try {
            CustomerCoupon customerCoupon = couponService.receiveCoupon(userId, id);
            return Result.success(customerCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get User's Coupons List")
    @GetMapping("/my")
    public Result<List<vtc.xueqing.flower.vo.CustomerCouponVO>> getUserCoupons(
            @ApiParam("User ID") @RequestParam Long userId,
            @ApiParam("Status: UNUSED-unused, USED-used, EXPIRED-expired") @RequestParam(required = false) String status
    ) {
        try {
            List<vtc.xueqing.flower.vo.CustomerCouponVO> customerCoupons = couponService.getUserCoupons(userId, status);
            return Result.success(customerCoupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Create Coupon (Merchant/Admin)")
    @PostMapping
    public Result<Coupon> createCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon createdCoupon = couponService.createCoupon(coupon);
            return Result.success(createdCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
