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
 * Customer entity class
 * Corresponds to database table: customers
 */
@Data
@TableName("customers")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * User ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * Username
     */
    @TableField("name")
    private String name;

    /**
     * Email
     */
    @TableField("email")
    private String email;

    /**
     * Phone number
     */
    @TableField("phone")
    private String phone;

    /**
     * Password (encrypted)
     */
    @TableField("password")
    private String password;

    /**
     * Account balance
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * Total membership points
     */
    @TableField("points")
    private Integer points;

    /**
     * Membership level: NORMAL-normal, VIP-VIP, SVIP-Super VIP
     */
    @TableField("level")
    private String level;

    /**
     * Gender
     */
    @TableField("gender")
    private String gender;

    /**
     * Default address
     */
    @TableField("address")
    private String address;

    /**
     * Email verification: 0-not verified, 1-verified
     */
    @TableField("email_verified")
    private Integer emailVerified;

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

    // Getter methods to provide field aliases for frontend
    
    /**
     * Get username (frontend field: username)
     */
    @JsonProperty("username")
    public String getUsername() {
        return this.name;
    }

    /**
     * Get membership level (frontend field: memberLevel)
     */
    @JsonProperty("memberLevel")
    public String getMemberLevel() {
        return this.level;
    }

    /**
     * Get creation time (frontend field: createTime)
     */
    @JsonProperty("createTime")
    public LocalDateTime getCreateTime() {
        return this.createDate;
    }
}
