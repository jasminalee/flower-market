package vtc.xueqing.flower.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vtc.xueqing.flower.entity.CustomerCoupon;
import vtc.xueqing.flower.vo.CustomerCouponVO;

import java.util.List;

/**
 * 用户优惠券Mapper接口
 */
public interface CustomerCouponMapper extends BaseMapper<CustomerCoupon> {
    
    /**
     * 获取用户优惠券列表（包含优惠券详细信息）
     */
    @Select("<script>" +
            "SELECT cc.id, cc.user_id, cc.coupon_id, cc.code, cc.status, " +
            "cc.receive_date, cc.used_date, cc.order_id, " +
            "c.name as coupon_name, c.type, c.value, c.min_price, " +
            "c.start_date, c.end_date, c.description " +
            "FROM customer_coupons cc " +
            "LEFT JOIN coupons c ON cc.coupon_id = c.coupon_id " +
            "WHERE cc.user_id = #{userId} " +
            "<if test='status != null and status != \"\"'>" +
            "AND cc.status = #{status} " +
            "</if>" +
            "ORDER BY cc.receive_date DESC" +
            "</script>")
    List<CustomerCouponVO> selectCouponListWithDetail(@Param("userId") Long userId, @Param("status") String status);
}
