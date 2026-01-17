package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Product traceability entity class
 * Corresponds to database table: product_trackability
 */
@Data
@TableName("product_trackability")
public class ProductTrackability implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Traceability ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Product ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * Origin
     */
    @TableField("origin")
    private String origin;

    /**
     * Planting method
     */
    @TableField("planting_method")
    private String plantingMethod;

    /**
     * Picking date
     */
    @TableField("picking_date")
    private LocalDate pickingDate;

    /**
     * Processing date
     */
    @TableField("proc_date")
    private LocalDate procDate;

    /**
     * Certification information
     */
    @TableField("certification")
    private String certification;

    /**
     * Traceability description
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
