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
 * 优惠券实体类
 * 对应数据库表：coupons
 */
@Data
@TableName("coupons")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    @TableId(value = "coupon_id", type = IdType.AUTO)
    private Long couponId;

    /**
     * 商家ID，NULL表示平台优惠券
     */
    @TableField("merch_id")
    private Long merchId;

    /**
     * 优惠券名称
     */
    @TableField("name")
    private String name;

    /**
     * 优惠券类型：DISCOUNT-折扣券，FULL_REDUCTION-满减券，FIXED_AMOUNT-固定金额券
     */
    @TableField("type")
    private String type;

    /**
     * 优惠值（折扣比例或金额）
     */
    @TableField("value")
    private BigDecimal value;

    /**
     * 最低消费金额
     */
    @TableField("min_price")
    private BigDecimal minPrice;

    /**
     * 发放总数量
     */
    @TableField("total_quantity")
    private Integer totalQuantity;

    /**
     * 已领取数量
     */
    @TableField("received_quantity")
    private Integer receivedQuantity;

    /**
     * 有效期开始
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 有效期结束
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 状态：ACTIVE-激活，INACTIVE-未激活，EXPIRED-已过期
     */
    @TableField("status")
    private String status;

    /**
     * 优惠券描述
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
