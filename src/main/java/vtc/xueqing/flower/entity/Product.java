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
 * 产品实体类
 * 对应数据库表：products
 */
@Data
@TableName("products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @TableId(value = "prod_id", type = IdType.AUTO)
    private Long prodId;

    /**
     * 商家ID
     */
    @TableField("merch_id")
    private Long merchId;

    /**
     * 分类ID
     */
    @TableField("cat_id")
    private Long catId;

    /**
     * 产品名称
     */
    @TableField("name")
    private String name;

    /**
     * 产品价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 销量
     */
    @TableField("sales")
    private Integer sales;

    /**
     * 产品主图
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * 产品图片集（JSON数组）
     */
    @TableField("images")
    private String images;

    /**
     * 产品描述
     */
    @TableField("description")
    private String description;

    /**
     * 产品状态：ACTIVE-上架，INACTIVE-下架，DELETED-已删除
     */
    @TableField("status")
    private String status;

    /**
     * 库存状态：IN_STOCK-有货，LOW_STOCK-库存不足，OUT_OF_STOCK-缺货
     */
    @TableField("stock_status")
    private String stockStatus;

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
