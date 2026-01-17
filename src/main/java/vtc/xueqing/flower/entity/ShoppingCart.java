package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Shopping cart entity class
 * Corresponds to database table: shopping_cart
 */
@Data
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Shopping cart ID
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
     * Quantity
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * Selected: 0-not selected, 1-selected
     */
    @TableField("selected")
    private Integer selected;

    /**
     * Add time
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * Update time
     */
    @TableField("update_date")
    private LocalDateTime updateDate;
}
