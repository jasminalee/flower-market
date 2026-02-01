package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.CheckIn;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.mapper.CheckInMapper;
import vtc.xueqing.flower.mapper.CustomerMapper;
import vtc.xueqing.flower.service.CheckInService;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Check-in Service Implementation Class
 */
@Service
public class CheckInServiceImpl implements CheckInService {
    
    @Resource
    private CheckInMapper checkInMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    // Check-in reward configuration
    private static final int NORMAL_REWARD = 10;  // Normal check-in reward points
    private static final int CONTINUOUS_REWARD = 5;  // Continuous check-in bonus points
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckIn dailyCheckIn(Long userId) {
        // 1. Check if user exists
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new RuntimeException("User does not exist");
        }
        
        // 2. Check if checked in today
        LocalDate today = LocalDate.now();
        if (isTodayCheckedIn(userId)) {
            throw new RuntimeException("Already checked in today");
        }
        
        // 3. Calculate consecutive check-in days
        int continuousDays = calculateContinuousDays(userId);
        
        // 4. Calculate reward points
        int rewardPoints = NORMAL_REWARD;
        if (continuousDays > 0) {
            rewardPoints += CONTINUOUS_REWARD;  // Continuous check-in bonus
        }
        
        // 5. Create check-in record
        CheckIn checkIn = new CheckIn();
        checkIn.setUserId(userId);
        checkIn.setCheckDate(today);
        checkIn.setContinuousDays(continuousDays + 1);
        checkIn.setRewardPoints(rewardPoints);
        checkInMapper.insert(checkIn);
        
        // 6. Update user points and balance
        // Update total points
        Integer currentPoints = customer.getPoints() == null ? 0 : customer.getPoints();
        customer.setPoints(currentPoints + rewardPoints);
        
        // Update balance (points can be converted to balance, simplified here)
        // 1 point = 0.1 yuan
        customer.setBalance(customer.getBalance().add(
            java.math.BigDecimal.valueOf(rewardPoints * 0.1)
        ));
        
        customerMapper.updateById(customer);
        
        return checkIn;
    }
    
    @Override
    public List<CheckIn> getCheckInHistory(Long userId, Integer limit) {
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckDate);
        
        if (limit != null && limit > 0) {
            wrapper.last("LIMIT " + limit);
        }
        
        return checkInMapper.selectList(wrapper);
    }
    
    @Override
    public Integer getContinuousDays(Long userId) {
        // Get most recent check-in record
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckDate)
                .last("LIMIT 1");
        
        CheckIn lastCheckIn = checkInMapper.selectOne(wrapper);
        
        if (lastCheckIn == null) {
            return 0;
        }
        
        // Check if the most recent check-in was yesterday or today
        LocalDate today = LocalDate.now();
        LocalDate lastCheckDate = lastCheckIn.getCheckDate();
        
        long daysBetween = ChronoUnit.DAYS.between(lastCheckDate, today);
        
        if (daysBetween <= 1) {
            // Checked in yesterday or today, return consecutive days
            return lastCheckIn.getContinuousDays();
        } else {
            // Interrupted, return 0
            return 0;
        }
    }
    
    @Override
    public boolean isTodayCheckedIn(Long userId) {
        LocalDate today = LocalDate.now();
        
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .eq(CheckIn::getCheckDate, today);
        
        Long count = checkInMapper.selectCount(wrapper);
        return count > 0;
    }
    
    /**
     * Calculate consecutive check-in days (excluding today)
     */
    private int calculateContinuousDays(Long userId) {
        // Get most recent check-in record
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckDate)
                .last("LIMIT 1");
        
        CheckIn lastCheckIn = checkInMapper.selectOne(wrapper);
        
        if (lastCheckIn == null) {
            return 0;  // First check-in
        }
        
        // Check if the most recent check-in was yesterday
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastCheckDate = lastCheckIn.getCheckDate();
        
        if (lastCheckDate.equals(yesterday)) {
            // Checked in yesterday, consecutive
            return lastCheckIn.getContinuousDays();
        } else {
            // Interrupted, restart
            return 0;
        }
    }
}
