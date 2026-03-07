package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Merchant entity class
 * Corresponds to database table: merchants
 */
@Data
@TableName("merchants")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Merchant ID
     */
    @TableId(value = "merch_id", type = IdType.AUTO)
    private Long merchId;

    /**
     * Merchant name
     */
    @TableField("name")
    private String name;

    /**
     * Merchant email
     */
    @TableField("email")
    private String email;

    /**
     * Password (encrypted)
     */
    @TableField("password")
    private String password;

    /**
     * Merchant phone
     */
    @TableField("phone")
    private String phone;

    /**
     * Contact name
     */
    @TableField("contact_name")
    private String contactName;

    /**
     * Business hours
     */
    @TableField("business_hours")
    private String businessHours;

    /**
     * Shop logo
     */
    @TableField("shop_logo")
    private String shopLogo;

    /**
     * Merchant qualification certificate
     */
    @TableField("qualification")
    private String qualification;

    /**
     * Merchant address
     */
    @TableField("address")
    private String address;

    /**
     * Shop description
     */
    @TableField("description")
    private String description;

    /**
     * Merchant status: PENDING-pending review, ACTIVE-active, SUSPENDED-suspended, REJECTED-rejected
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
