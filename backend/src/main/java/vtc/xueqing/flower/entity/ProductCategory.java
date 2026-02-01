package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product category entity class
 * Corresponds to database table: product_categories
 */
@Data
@TableName("product_categories")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Category ID
     */
    @TableId(value = "cate_id", type = IdType.AUTO)
    private Long cateId;

    /**
     * Category name
     */
    @TableField("name")
    private String name;

    /**
     * Parent category ID, 0 means top-level category
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * Sort order
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * Category icon
     */
    @TableField("icon")
    private String icon;

    /**
     * Category description
     */
    @TableField("description")
    private String description;

    /**
     * Creation time
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * Update time
     */
    @TableField("update_date")
    private LocalDateTime updateDate;
}
