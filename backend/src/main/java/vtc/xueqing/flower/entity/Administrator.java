package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Administrator entity class
 * Corresponds to database table: administrators
 */
@Data
@TableName("administrators")
public class Administrator implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Administrator ID
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long adminId;

    /**
     * Administrator name
     */
    @TableField("name")
    private String name;

    /**
     * Password (encrypted)
     */
    @TableField("password")
    private String password;

    /**
     * Email
     */
    @TableField("email")
    private String email;

    /**
     * Permission level: SUPER_ADMIN-super administrator, ADMIN-administrator
     */
    @TableField("permission")
    private String permission;

    /**
     * Status: ACTIVE-active, INACTIVE-disabled
     */
    @TableField("status")
    private String status;

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
