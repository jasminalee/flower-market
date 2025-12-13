package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Coupon;
import vtc.xueqing.flower.entity.CustomerCoupon;

import java.util.List;

/**
 * 优惠券服务接口
 */
public interface CouponService {
    
    /**
     * 获取可用优惠券列表（分页）
     * @param page 分页信息
     * @param merchId 商家ID（可选，null表示获取所有）
     * @return 优惠券列表
     */
    IPage<Coupon> getAvailableCoupons(Page<Coupon> page, Long merchId);
    
    /**
     * 用户领取优惠券
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 用户优惠券
     */
    CustomerCoupon receiveCoupon(Long userId, Long couponId);
    
    /**
     * 获取用户的优惠券列表
     * @param userId 用户ID
     * @param status 状态（可选）：UNUSED-未使用，USED-已使用，EXPIRED-已过期
     * @return 用户优惠券列表
     */
    List<CustomerCoupon> getUserCoupons(Long userId, String status);
    
    /**
     * 使用优惠券（在订单支付时调用）
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @param orderId 订单ID
     * @return 用户优惠券
     */
    CustomerCoupon useCoupon(Long userId, Long couponId, Long orderId);
    
    /**
     * 创建优惠券（商家/管理员）
     * @param coupon 优惠券信息
     * @return 创建的优惠券
     */
    Coupon createCoupon(Coupon coupon);
}
