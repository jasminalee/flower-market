package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Coupon;
import vtc.xueqing.flower.entity.CustomerCoupon;
import vtc.xueqing.flower.mapper.CouponMapper;
import vtc.xueqing.flower.mapper.CustomerCouponMapper;
import vtc.xueqing.flower.service.CouponService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Coupon Service Implementation Class
 */
@Service
public class CouponServiceImpl implements CouponService {
    
    @Resource
    private CouponMapper couponMapper;
    
    @Resource
    private CustomerCouponMapper customerCouponMapper;
    
    @Override
    public IPage<Coupon> getAvailableCoupons(Page<Coupon> page, Long merchId) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        
        LocalDateTime now = LocalDateTime.now();
        
        // Filter conditions
        wrapper.eq(merchId != null, Coupon::getMerchId, merchId)
                .eq(Coupon::getStatus, "ACTIVE")
                .le(Coupon::getStartDate, now) // Start date <= current time
                .ge(Coupon::getEndDate, now)   // End date >= current time
                .apply("total_quantity > received_quantity") // Still in stock
                .orderByDesc(Coupon::getCreateDate);
        
        return couponMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerCoupon receiveCoupon(Long userId, Long couponId) {
        // 1. Get coupon information
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("Coupon does not exist");
        }
        
        // 2. Check coupon status
        if (!"ACTIVE".equals(coupon.getStatus())) {
            throw new RuntimeException("Coupon not activated");
        }
        
        // 3. Check validity period
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartDate())) {
            throw new RuntimeException("Coupon not started yet");
        }
        if (now.isAfter(coupon.getEndDate())) {
            throw new RuntimeException("Coupon has expired");
        }
        
        // 4. Check inventory
        if (coupon.getReceivedQuantity() >= coupon.getTotalQuantity()) {
            throw new RuntimeException("Coupon has been claimed out");
        }
        
        // 5. Check if user has already claimed
        LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerCoupon::getUserId, userId)
                .eq(CustomerCoupon::getCouponId, couponId);
        Long count = customerCouponMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("You have already claimed this coupon");
        }
        
        // 6. Create user coupon record
        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setUserId(userId);
        customerCoupon.setCouponId(couponId);
        customerCoupon.setStatus("UNUSED");
        customerCoupon.setReceiveDate(LocalDateTime.now());
        customerCouponMapper.insert(customerCoupon);
        
        // 7. Update coupon claimed quantity
        coupon.setReceivedQuantity(coupon.getReceivedQuantity() + 1);
        couponMapper.updateById(coupon);
        
        return customerCoupon;
    }
    
    @Override
    public List<vtc.xueqing.flower.vo.CustomerCouponVO> getUserCoupons(Long userId, String status) {
        return customerCouponMapper.selectCouponListWithDetail(userId, status);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerCoupon useCoupon(Long userId, Long couponId, Long orderId) {
        // 1. Find user's coupon
        LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerCoupon::getUserId, userId)
                .eq(CustomerCoupon::getCouponId, couponId)
                .eq(CustomerCoupon::getStatus, "UNUSED");
        CustomerCoupon customerCoupon = customerCouponMapper.selectOne(wrapper);
        
        if (customerCoupon == null) {
            throw new RuntimeException("Coupon does not exist or has been used");
        }
        
        // 2. Check if coupon has expired
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("Coupon does not exist");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(coupon.getEndDate())) {
            // Mark as expired
            customerCoupon.setStatus("EXPIRED");
            customerCouponMapper.updateById(customerCoupon);
            throw new RuntimeException("Coupon has expired");
        }
        
        // 3. Use coupon
        customerCoupon.setStatus("USED");
        customerCoupon.setUsedDate(LocalDateTime.now());
        customerCoupon.setOrderId(orderId);
        customerCouponMapper.updateById(customerCoupon);
        
        return customerCoupon;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Coupon createCoupon(Coupon coupon) {
        // Set initial values
        if (coupon.getReceivedQuantity() == null) {
            coupon.setReceivedQuantity(0);
        }
        
        if (coupon.getStatus() == null || coupon.getStatus().isEmpty()) {
            coupon.setStatus("ACTIVE");
        }
        
        // Validate data
        if (coupon.getTotalQuantity() <= 0) {
            throw new RuntimeException("Distribution quantity must be greater than 0");
        }
        
        if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
            throw new RuntimeException("Start time cannot be later than end time");
        }
        
        couponMapper.insert(coupon);
        return coupon;
    }
}
