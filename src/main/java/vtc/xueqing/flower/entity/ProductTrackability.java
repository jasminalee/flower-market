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
 * 产品溯源实体类
 * 对应数据库表：product_trackability
 */
@Data
@TableName("product_trackability")
public class ProductTrackability implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 溯源ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品ID
     */
    @TableField("prod_id")
    private Long prodId;

    /**
     * 产地
     */
    @TableField("origin")
    private String origin;

    /**
     * 种植方式
     */
    @TableField("planting_method")
    private String plantingMethod;

    /**
     * 采摘日期
     */
    @TableField("picking_date")
    private LocalDate pickingDate;

    /**
     * 加工日期
     */
    @TableField("proc_date")
    private LocalDate procDate;

    /**
     * 认证信息
     */
    @TableField("certification")
    private String certification;

    /**
     * 溯源描述
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
