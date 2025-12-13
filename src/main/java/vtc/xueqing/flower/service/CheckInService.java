package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.CheckIn;

import java.util.List;

/**
 * 签到服务接口
 */
public interface CheckInService {
    
    /**
     * 每日签到
     * @param userId 用户ID
     * @return 签到记录
     */
    CheckIn dailyCheckIn(Long userId);
    
    /**
     * 获取用户签到历史
     * @param userId 用户ID
     * @param limit 查询数量限制
     * @return 签到记录列表
     */
    List<CheckIn> getCheckInHistory(Long userId, Integer limit);
    
    /**
     * 获取用户连续签到天数
     * @param userId 用户ID
     * @return 连续签到天数
     */
    Integer getContinuousDays(Long userId);
    
    /**
     * 检查今日是否已签到
     * @param userId 用户ID
     * @return 是否已签到
     */
    boolean isTodayCheckedIn(Long userId);
}
