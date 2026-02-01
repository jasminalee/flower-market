package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * System configuration entity class
 * Corresponds to database table: system_configuration
 */
@Data
@TableName("system_configuration")
public class SystemConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Configuration ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Configuration key
     */
    @TableField("config_key")
    private String configKey;

    /**
     * Configuration value
     */
    @TableField("config_value")
    private String configValue;

    /**
     * Configuration description
     */
    @TableField("description")
    private String description;

    /**
     * Configuration category
     */
    @TableField("category")
    private String category;

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
