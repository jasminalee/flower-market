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
 * Order Mapper Interface
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * Query order list with customer and merchant names
     *
     * @param page pagination object
     * @param userId user ID (optional)
     * @param merchId merchant ID (optional)
     * @param status order status (optional)
     * @return order VO pagination list
     */
    @Select("<script>" +
            "SELECT o.*, c.name AS customer_name, m.name AS merchant_name " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.user_id = c.user_id " +
            "LEFT JOIN merchants m ON o.merch_id = m.merch_id " +
            "<where>" +
            "  <if test='userId != null'>" +
            "    AND o.user_id = #{userId} " +
            "  </if>" +
            "  <if test='merchId != null'>" +
            "    AND o.merch_id = #{merchId} " +
            "  </if>" +
            "  <if test='status != null and status != \"\"'>" +
            "    AND o.status = #{status} " +
            "  </if>" +
            "</where> " +
            "ORDER BY o.order_date DESC" +
            "</script>")
    IPage<OrderVO> selectOrdersWithMerchant(Page<OrderVO> page,
                                            @Param("userId") Long userId,
                                            @Param("merchId") Long merchId,
                                            @Param("status") String status);

    /**
     * Query all orders (with customer and merchant names) - for administrators
     * 
     * @param page pagination object
     * @param status order status (optional)
     * @param keyword search keyword: order number or customer name (optional)
     * @return order VO pagination list
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
     * Query order details by order ID (with customer and merchant names)
     * 
     * @param orderId order ID
     * @return order detail VO
     */
    @Select("SELECT o.*, c.name AS customer_name, c.phone AS customer_phone, m.name AS merchant_name " +
            "FROM orders o " +
            "LEFT JOIN customers c ON o.user_id = c.user_id " +
            "LEFT JOIN merchants m ON o.merch_id = m.merch_id " +
            "WHERE o.id = #{orderId}")
    OrderDetailVO selectOrderDetailById(@Param("orderId") Long orderId);
}
