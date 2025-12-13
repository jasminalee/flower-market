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
 * 签到服务实现类
 */
@Service
public class CheckInServiceImpl implements CheckInService {
    
    @Resource
    private CheckInMapper checkInMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    // 签到奖励配置
    private static final int NORMAL_REWARD = 10;  // 普通签到奖励积分
    private static final int CONTINUOUS_REWARD = 5;  // 连续签到额外奖励积分
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckIn dailyCheckIn(Long userId) {
        // 1. 检查用户是否存在
        Customer customer = customerMapper.selectById(userId);
        if (customer == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 2. 检查今日是否已签到
        LocalDate today = LocalDate.now();
        if (isTodayCheckedIn(userId)) {
            throw new RuntimeException("今日已签到");
        }
        
        // 3. 计算连续签到天数
        int continuousDays = calculateContinuousDays(userId);
        
        // 4. 计算奖励积分
        int rewardPoints = NORMAL_REWARD;
        if (continuousDays > 0) {
            rewardPoints += CONTINUOUS_REWARD;  // 连续签到额外奖励
        }
        
        // 5. 创建签到记录
        CheckIn checkIn = new CheckIn();
        checkIn.setUserId(userId);
        checkIn.setCheckDate(today);
        checkIn.setContinuousDays(continuousDays + 1);
        checkIn.setRewardPoints(rewardPoints);
        checkInMapper.insert(checkIn);
        
        // 6. 更新用户余额（积分可以转换为余额，这里简化处理）
        // 实际项目中可以单独设计积分表
        // 这里简单处理：1积分 = 0.1元
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
        // 获取最近的签到记录
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckDate)
                .last("LIMIT 1");
        
        CheckIn lastCheckIn = checkInMapper.selectOne(wrapper);
        
        if (lastCheckIn == null) {
            return 0;
        }
        
        // 检查最近签到是否是昨天或今天
        LocalDate today = LocalDate.now();
        LocalDate lastCheckDate = lastCheckIn.getCheckDate();
        
        long daysBetween = ChronoUnit.DAYS.between(lastCheckDate, today);
        
        if (daysBetween <= 1) {
            // 昨天或今天签到过，返回连续天数
            return lastCheckIn.getContinuousDays();
        } else {
            // 中断了，返回0
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
     * 计算连续签到天数（不包括今天）
     */
    private int calculateContinuousDays(Long userId) {
        // 获取最近的签到记录
        LambdaQueryWrapper<CheckIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CheckIn::getUserId, userId)
                .orderByDesc(CheckIn::getCheckDate)
                .last("LIMIT 1");
        
        CheckIn lastCheckIn = checkInMapper.selectOne(wrapper);
        
        if (lastCheckIn == null) {
            return 0;  // 首次签到
        }
        
        // 检查最近签到是否是昨天
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate lastCheckDate = lastCheckIn.getCheckDate();
        
        if (lastCheckDate.equals(yesterday)) {
            // 昨天签到过，连续
            return lastCheckIn.getContinuousDays();
        } else {
            // 中断了，重新开始
            return 0;
        }
    }
}
