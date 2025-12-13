package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商家实体类
 * 对应数据库表：merchants
 */
@Data
@TableName("merchants")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商家ID
     */
    @TableId(value = "merch_id", type = IdType.AUTO)
    private Long merchId;

    /**
     * 商家名称
     */
    @TableField("name")
    private String name;

    /**
     * 商家邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 密码（加密）
     */
    @TableField("password")
    private String password;

    /**
     * 商家电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 店铺logo
     */
    @TableField("shop_logo")
    private String shopLogo;

    /**
     * 商家资质证明
     */
    @TableField("qualification")
    private String qualification;

    /**
     * 商家地址
     */
    @TableField("address")
    private String address;

    /**
     * 店铺描述
     */
    @TableField("description")
    private String description;

    /**
     * 商家状态：PENDING-待审核，ACTIVE-正常，SUSPENDED-暂停，REJECTED-已拒绝
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
