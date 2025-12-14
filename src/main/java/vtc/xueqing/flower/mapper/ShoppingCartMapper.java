package vtc.xueqing.flower.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import vtc.xueqing.flower.entity.ShoppingCart;
import vtc.xueqing.flower.vo.ShoppingCartVO;

import java.util.List;

/**
 * 购物车Mapper接口
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    
    /**
     * 获取用户购物车列表（包含商品信息）
     */
    @Select("SELECT sc.id, sc.user_id, sc.prod_id, sc.quantity, sc.selected, " +
            "sc.create_date, sc.update_date, " +
            "p.name as prod_name, p.description as prod_desc, p.main_image as prod_image, " +
            "p.price as prod_price, p.stock as prod_stock " +
            "FROM shopping_cart sc " +
            "LEFT JOIN products p ON sc.prod_id = p.prod_id " +
            "WHERE sc.user_id = #{userId} " +
            "ORDER BY sc.create_date DESC")
    List<ShoppingCartVO> selectCartWithProductByUserId(Long userId);
}
