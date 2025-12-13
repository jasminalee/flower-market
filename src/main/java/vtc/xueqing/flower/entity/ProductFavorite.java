package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品收藏实体类
 * 对应数据库表：product_favorites
 */
@Data
@TableName("product_favorites")
public class ProductFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 产品ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * 收藏时间
     */
    @TableField("fav_date")
    private LocalDateTime favDate;
}
