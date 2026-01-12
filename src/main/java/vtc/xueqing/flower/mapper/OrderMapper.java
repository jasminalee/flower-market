package vtc.xueqing.flower.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vtc.xueqing.flower.entity.Order;
import vtc.xueqing.flower.vo.OrderDetailVO;
import vtc.xueqing.flower.vo.OrderVO;

/**
 * 订单Mapper接口
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询所有订单（带客户和商家名称）- 管理员用
     * 
     * @param page 分页对象
     * @param status 订单状态（可选）
     * @param keyword 搜索关键词：订单号或客户名（可选）
     * @return 订单VO分页列表
     */
    @Select("<script>" +
            "SELECT o.*, c.name AS customer_name, m.name AS merchant_name " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.user_id = c.user_id " +
            "LEFT JOIN merchants m ON o.merch_id = m.merch_id " +
            "<where>" +
            "  <if test='status != null and status != \"\"'>" +
            "    AND o.status = #{status}" +
            "  </if>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (o.order_no LIKE CONCAT('%', #{keyword}, '%') " +
            "         OR c.name LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "</where>" +
            "ORDER BY o.order_date DESC" +
            "</script>")
    IPage<OrderVO> selectAllOrdersWithDetail(Page<OrderVO> page, 
                                              @Param("status") String status, 
                                              @Param("keyword") String keyword);
    
    /**
     * 根据订单ID查询订单详情（带客户和商家名称）
     * 
     * @param orderId 订单ID
     * @return 订单详情VO
     */
    @Select("SELECT o.*, c.name AS customer_name, c.phone AS customer_phone, m.name AS merchant_name " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.user_id = c.user_id " +
            "LEFT JOIN merchants m ON o.merch_id = m.merch_id " +
            "WHERE o.id = #{orderId}")
    OrderDetailVO selectOrderDetailById(@Param("orderId") Long orderId);
}
