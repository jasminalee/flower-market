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
 * 优惠券控制器
 */
@Api(tags = "优惠券管理")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    
    @Resource
    private CouponService couponService;
    
    @ApiOperation("获取可用优惠券列表（分页）")
    @GetMapping
    public Result<IPage<Coupon>> getAvailableCoupons(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("商家ID") @RequestParam(required = false) Long merchId
    ) {
        try {
            Page<Coupon> page = new Page<>(current, size);
            IPage<Coupon> couponPage = couponService.getAvailableCoupons(page, merchId);
            return Result.success(couponPage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("用户领取优惠券")
    @PostMapping("/{id}/receive")
    public Result<CustomerCoupon> receiveCoupon(
            @PathVariable("id") Long id,
            @ApiParam("用户ID") @RequestParam Long userId
    ) {
        try {
            CustomerCoupon customerCoupon = couponService.receiveCoupon(userId, id);
            return Result.success(customerCoupon);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取用户的优惠券列表")
    @GetMapping("/my")
    public Result<List<CustomerCoupon>> getUserCoupons(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("状态：UNUSED-未使用，USED-已使用，EXPIRED-已过期") @RequestParam(required = false) String status
    ) {
        try {
            List<CustomerCoupon> customerCoupons = couponService.getUserCoupons(userId, status);
            return Result.success(customerCoupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("创建优惠券（商家/管理员）")
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
