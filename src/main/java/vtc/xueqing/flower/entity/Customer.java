package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 顾客实体类
 * 对应数据库表：customers
 */
@Data
@TableName("customers")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField("name")
    private String name;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 密码（加密）
     */
    @TableField("password")
    private String password;

    /**
     * 账户余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 会员总积分
     */
    @TableField("points")
    private Integer points;

    /**
     * 会员等级：NORMAL-普通，VIP-VIP，SVIP-超级VIP
     */
    @TableField("level")
    private String level;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 默认地址
     */
    @TableField("address")
    private String address;

    /**
     * 邮箱是否验证：0-未验证，1-已验证
     */
    @TableField("email_verified")
    private Integer emailVerified;

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

    // 为前端提供字段别名的getter方法
    
    /**
     * 获取用户名（前端字段：username）
     */
    @JsonProperty("username")
    public String getUsername() {
        return this.name;
    }

    /**
     * 获取会员等级（前端字段：memberLevel）
     */
    @JsonProperty("memberLevel")
    public String getMemberLevel() {
        return this.level;
    }

    /**
     * 获取创建时间（前端字段：createTime）
     */
    @JsonProperty("createTime")
    public LocalDateTime getCreateTime() {
        return this.createDate;
    }
}
