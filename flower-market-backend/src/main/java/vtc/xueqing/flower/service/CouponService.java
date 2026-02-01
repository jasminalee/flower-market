package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Coupon;
import vtc.xueqing.flower.entity.CustomerCoupon;

import java.util.List;

/**
 * Coupon service interface.
 */
public interface CouponService {
    
    /**
     * Get available coupon list (paginated).
     * @param page pagination info
     * @param merchId merchant ID (optional, null for all)
     * @return coupon list
     */
    IPage<Coupon> getAvailableCoupons(Page<Coupon> page, Long merchId);
    
    /**
     * User claims a coupon.
     * @param userId user ID
     * @param couponId coupon ID
     * @return customer coupon
     */
    CustomerCoupon receiveCoupon(Long userId, Long couponId);
    
    /**
     * Get user's coupons with details.
     * @param userId user ID
     * @param status status (optional): UNUSED, USED, EXPIRED
     * @return user coupon list
     */
    List<vtc.xueqing.flower.vo.CustomerCouponVO> getUserCoupons(Long userId, String status);
    
    /**
     * Use coupon (during order payment).
     * @param userId user ID
     * @param couponId coupon ID
     * @param orderId order ID
     * @return customer coupon
     */
    CustomerCoupon useCoupon(Long userId, Long couponId, Long orderId);
    
    /**
     * Create coupon (merchant/admin).
     * @param coupon coupon info
     * @return created coupon
     */
    Coupon createCoupon(Coupon coupon);
}
