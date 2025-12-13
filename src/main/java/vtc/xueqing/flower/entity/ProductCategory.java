package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 产品分类实体类
 * 对应数据库表：product_categories
 */
@Data
@TableName("product_categories")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @TableId(value = "cate_id", type = IdType.AUTO)
    private Long cateId;

    /**
     * 分类名称
     */
    @TableField("name")
    private String name;

    /**
     * 父分类ID，0表示顶级分类
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 排序顺序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 分类图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 分类描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;
}
