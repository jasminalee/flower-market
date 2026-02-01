package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.CheckIn;
import vtc.xueqing.flower.service.CheckInService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Check-in Controller
 */
@Api(tags = "Check-in Management")
@RestController
@RequestMapping("/api/customer/checkin")
public class CheckInController {
    
    @Resource
    private CheckInService checkInService;
    
    @ApiOperation("Daily Check-in")
    @PostMapping
    public Result<CheckIn> dailyCheckIn(@ApiParam("User ID") @RequestParam Long userId) {
        try {
            CheckIn checkIn = checkInService.dailyCheckIn(userId);
            return Result.success(checkIn);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Check-in History")
    @GetMapping("/history")
    public Result<List<CheckIn>> getCheckInHistory(
            @ApiParam("User ID") @RequestParam Long userId,
            @ApiParam("Query Limit") @RequestParam(defaultValue = "30") Integer limit
    ) {
        try {
            List<CheckIn> history = checkInService.getCheckInHistory(userId, limit);
            return Result.success(history);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Check-in Statistics")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getCheckInStats(@ApiParam("User ID") @RequestParam Long userId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("continuousDays", checkInService.getContinuousDays(userId));
            stats.put("todayCheckedIn", checkInService.isTodayCheckedIn(userId));
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
