package vtc.xueqing.flower.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import vtc.xueqing.flower.entity.ProductReview;
import vtc.xueqing.flower.vo.ProductReviewVO;

/**
 * 产品评价Mapper接口
 */
public interface ProductReviewMapper extends BaseMapper<ProductReview> {
    
    /**
     * 获取所有评价列表（管理员）- 包含关联信息
     * @param page 分页对象
     * @param status 审核状态
     * @return 评价VO列表
     */
    @Select("<script>" +
            "SELECT " +
            "  pr.id, pr.user_id, pr.prod_id, pr.order_id, " +
            "  pr.rating, pr.content, pr.images, pr.verified, " +
            "  pr.status, pr.create_date, " +
            "  c.name AS user_name, " +
            "  p.name AS product_name, " +
            "  p.main_image AS product_image " +
            "FROM product_reviews pr " +
            "LEFT JOIN customers c ON pr.user_id = c.user_id " +
            "LEFT JOIN products p ON pr.prod_id = p.prod_id " +
            "<where>" +
            "  <if test='status != null and status != \"\"'>" +
            "    AND pr.status = #{status}" +
            "  </if>" +
            "</where>" +
            "ORDER BY pr.create_date DESC" +
            "</script>")
    IPage<ProductReviewVO> selectAllReviewsWithDetail(Page<ProductReviewVO> page, @Param("status") String status);
}
