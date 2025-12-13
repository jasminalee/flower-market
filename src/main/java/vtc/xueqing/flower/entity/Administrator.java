package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员实体类
 * 对应数据库表：administrators
 */
@Data
@TableName("administrators")
public class Administrator implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员ID
     */
    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long adminId;

    /**
     * 管理员姓名
     */
    @TableField("name")
    private String name;

    /**
     * 密码（加密）
     */
    @TableField("password")
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 权限等级：SUPER_ADMIN-超级管理员，ADMIN-管理员
     */
    @TableField("permission")
    private String permission;

    /**
     * 状态：ACTIVE-正常，INACTIVE-禁用
     */
    @TableField("status")
    private String status;

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
