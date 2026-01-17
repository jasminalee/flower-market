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
 * Check-in entity class
 * Corresponds to database table: check_ins
 */
@Data
@TableName("check_ins")
public class CheckIn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Check-in ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * User ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * Check-in date
     */
    @TableField("check_date")
    private LocalDate checkDate;

    /**
     * Consecutive check-in days
     */
    @TableField("continuous_days")
    private Integer continuousDays;

    /**
     * Reward points
     */
    @TableField("reward_points")
    private Integer rewardPoints;

    /**
     * Check-in time
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}
