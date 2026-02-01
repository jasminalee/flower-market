package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product favorite entity class
 * Corresponds to database table: product_favorites
 */
@Data
@TableName("product_favorites")
public class ProductFavorite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Favorite ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Product ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * Favorite time
     */
    @TableField("fav_date")
    private LocalDateTime favDate;
}
