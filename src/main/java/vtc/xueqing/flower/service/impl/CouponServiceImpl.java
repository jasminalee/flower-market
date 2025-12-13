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
 * 优惠券服务实现类
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
        
        // 筛选条件
        wrapper.eq(merchId != null, Coupon::getMerchId, merchId)
                .eq(Coupon::getStatus, "ACTIVE")
                .le(Coupon::getStartDate, now) // 开始时间 <= 当前时间
                .ge(Coupon::getEndDate, now)   // 结束时间 >= 当前时间
                .apply("total_quantity > received_quantity") // 还有库存
                .orderByDesc(Coupon::getCreateDate);
        
        return couponMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerCoupon receiveCoupon(Long userId, Long couponId) {
        // 1. 获取优惠券信息
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        
        // 2. 检查优惠券状态
        if (!"ACTIVE".equals(coupon.getStatus())) {
            throw new RuntimeException("优惠券未激活");
        }
        
        // 3. 检查有效期
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartDate())) {
            throw new RuntimeException("优惠券尚未开始");
        }
        if (now.isAfter(coupon.getEndDate())) {
            throw new RuntimeException("优惠券已过期");
        }
        
        // 4. 检查库存
        if (coupon.getReceivedQuantity() >= coupon.getTotalQuantity()) {
            throw new RuntimeException("优惠券已被领完");
        }
        
        // 5. 检查用户是否已领取过
        LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerCoupon::getUserId, userId)
                .eq(CustomerCoupon::getCouponId, couponId);
        Long count = customerCouponMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("您已领取过此优惠券");
        }
        
        // 6. 创建用户优惠券记录
        CustomerCoupon customerCoupon = new CustomerCoupon();
        customerCoupon.setUserId(userId);
        customerCoupon.setCouponId(couponId);
        customerCoupon.setStatus("UNUSED");
        customerCoupon.setReceiveDate(LocalDateTime.now());
        customerCouponMapper.insert(customerCoupon);
        
        // 7. 更新优惠券已领取数量
        coupon.setReceivedQuantity(coupon.getReceivedQuantity() + 1);
        couponMapper.updateById(coupon);
        
        return customerCoupon;
    }
    
    @Override
    public List<CustomerCoupon> getUserCoupons(Long userId, String status) {
        LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerCoupon::getUserId, userId)
                .eq(status != null && !status.isEmpty(), CustomerCoupon::getStatus, status)
                .orderByDesc(CustomerCoupon::getReceiveDate);
        
        return customerCouponMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomerCoupon useCoupon(Long userId, Long couponId, Long orderId) {
        // 1. 查找用户的优惠券
        LambdaQueryWrapper<CustomerCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerCoupon::getUserId, userId)
                .eq(CustomerCoupon::getCouponId, couponId)
                .eq(CustomerCoupon::getStatus, "UNUSED");
        CustomerCoupon customerCoupon = customerCouponMapper.selectOne(wrapper);
        
        if (customerCoupon == null) {
            throw new RuntimeException("优惠券不存在或已使用");
        }
        
        // 2. 检查优惠券是否过期
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(coupon.getEndDate())) {
            // 标记为已过期
            customerCoupon.setStatus("EXPIRED");
            customerCouponMapper.updateById(customerCoupon);
            throw new RuntimeException("优惠券已过期");
        }
        
        // 3. 使用优惠券
        customerCoupon.setStatus("USED");
        customerCoupon.setUsedDate(LocalDateTime.now());
        customerCoupon.setOrderId(orderId);
        customerCouponMapper.updateById(customerCoupon);
        
        return customerCoupon;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Coupon createCoupon(Coupon coupon) {
        // 设置初始值
        if (coupon.getReceivedQuantity() == null) {
            coupon.setReceivedQuantity(0);
        }
        
        if (coupon.getStatus() == null || coupon.getStatus().isEmpty()) {
            coupon.setStatus("ACTIVE");
        }
        
        // 验证数据
        if (coupon.getTotalQuantity() <= 0) {
            throw new RuntimeException("发放数量必须大于0");
        }
        
        if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }
        
        couponMapper.insert(coupon);
        return coupon;
    }
}
