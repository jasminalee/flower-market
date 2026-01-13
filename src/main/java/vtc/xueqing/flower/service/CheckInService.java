package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.CheckIn;

import java.util.List;

/**
 * Check-in service interface.
 */
public interface CheckInService {
    
    /**
     * Daily check-in.
     * @param userId user ID
     * @return check-in record
     */
    CheckIn dailyCheckIn(Long userId);
    
    /**
     * Get user check-in history.
     * @param userId user ID
     * @param limit record limit
     * @return check-in records
     */
    List<CheckIn> getCheckInHistory(Long userId, Integer limit);
    
    /**
     * Get user's continuous check-in days.
     * @param userId user ID
     * @return continuous days
     */
    Integer getContinuousDays(Long userId);
    
    /**
     * 检查今日是否已签到
     * @param userId 用户ID
     * @return 是否已签到
    /**
     * Check if user has checked in today.
     * @param userId user ID
     * @return whether already checked in
     */
    boolean isTodayCheckedIn(Long userId);
}
