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
 * 签到实体类
 * 对应数据库表：check_ins
 */
@Data
@TableName("check_ins")
public class CheckIn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签到ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 签到日期
     */
    @TableField("check_date")
    private LocalDate checkDate;

    /**
     * 连续签到天数
     */
    @TableField("continuous_days")
    private Integer continuousDays;

    /**
     * 奖励积分
     */
    @TableField("reward_points")
    private Integer rewardPoints;

    /**
     * 签到时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
