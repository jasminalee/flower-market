package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order item entity class
 * Corresponds to database table: order_items
 */
@Data
@TableName("order_items")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Order item ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Order ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * Product ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * Product name
     */
    @TableField("name")
    private String name;

    /**
     * Product image
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * Quantity purchased
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * Unit price
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * Subtotal
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * Creation time
     */
    @TableField("create_date")
    private LocalDateTime createDate;
}
