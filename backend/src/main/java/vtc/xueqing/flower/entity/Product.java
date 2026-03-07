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
 * Product entity class
 * Corresponds to database table: products
 */
@Data
@TableName("products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Product ID
     */
    @TableId(value = "prod_id", type = IdType.AUTO)
    private Long prodId;

    /**
     * Merchant ID
     */
    @TableField("merch_id")
    private Long merchId;

    /**
     * Merchant name (filled by association query)
     */
    @TableField(exist = false)
    private String merchantName;

    /**
     * Category ID
     */
    @TableField("cat_id")
    private Long catId;

    /**
     * Supplier ID
     */
    @TableField("supplier_id")
    private Long supplierId;

    /**
     * Supplier Name (filled by association query)
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * Supplier Rating (filled by association query)
     */
    @TableField(exist = false)
    private Double supplierRating;

    /**
     * Product name
     */
    @TableField("name")
    private String name;

    /**
     * Product price
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * Stock quantity
     */
    @TableField("stock")
    private Integer stock;

    /**
     * Sales volume
     */
    @TableField("sales")
    private Integer sales;

    /**
     * Product main image
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * Product image collection (JSON array)
     */
    @TableField("images")
    private String images;

    /**
     * Product description
     */
    @TableField("description")
    private String description;

    /**
     * Flowering period (e.g., 7-10 days)
     */
    @TableField("flowering_period")
    private String floweringPeriod;

    /**
     * Care difficulty: EASY, MEDIUM, HARD
     */
    @TableField("care_difficulty")
    private String careDifficulty;

    /**
     * Suitable environment details (temperature, light, humidity)
     */
    @TableField("suitable_environment")
    private String suitableEnvironment;

    /**
     * Flower language (meaning)
     */
    @TableField("floral_language")
    private String floralLanguage;

    /**
     * Product status: ACTIVE-listed, INACTIVE-unlisted, DELETED-deleted
     */
    @TableField("status")
    private String status;

    /**
     * Stock status: IN_STOCK-in stock, LOW_STOCK-low stock, OUT_OF_STOCK-out of stock
     */
    @TableField("stock_status")
    private String stockStatus;

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
