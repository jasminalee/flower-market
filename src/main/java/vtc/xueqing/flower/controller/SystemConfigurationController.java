package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.SystemConfiguration;
import vtc.xueqing.flower.service.SystemConfigurationService;

import java.util.List;

/**
 * 系统配置控制器
 */
@Api(tags = "系统配置管理")
@RestController
@RequestMapping("/api/admin/config")
public class SystemConfigurationController {
    
    @Autowired
    private SystemConfigurationService systemConfigurationService;
    
    @ApiOperation("获取系统配置列表（分页）")
    @GetMapping
    public Result<Page<SystemConfiguration>> getConfigList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category) {
        Page<SystemConfiguration> page = systemConfigurationService.getConfigList(pageNum, pageSize, category);
        return Result.success(page);
    }
    
    @ApiOperation("根据配置键获取配置")
    @GetMapping("/key/{configKey}")
    public Result<SystemConfiguration> getByKey(@PathVariable String configKey) {
        SystemConfiguration config = systemConfigurationService.getByKey(configKey);
        if (config == null) {
            return Result.error("配置不存在");
        }
        return Result.success(config);
    }
    
    @ApiOperation("根据分类获取配置列表")
    @GetMapping("/category/{category}")
    public Result<List<SystemConfiguration>> getByCategory(@PathVariable String category) {
        List<SystemConfiguration> configs = systemConfigurationService.getByCategory(category);
        return Result.success(configs);
    }
    
    @ApiOperation("创建系统配置")
    @PostMapping
    public Result<String> createConfig(@RequestBody SystemConfiguration config) {
        try {
            boolean success = systemConfigurationService.createConfig(config);
            if (success) {
                return Result.success("创建成功");
            }
            return Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新系统配置")
    @PutMapping("/{id}")
    public Result<String> updateConfig(@PathVariable Long id, @RequestBody SystemConfiguration config) {
        try {
            config.setId(id);
            boolean success = systemConfigurationService.updateConfig(config);
            if (success) {
                return Result.success("更新成功");
            }
            return Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除系统配置")
    @DeleteMapping("/{id}")
    public Result<String> deleteConfig(@PathVariable Long id) {
        boolean success = systemConfigurationService.deleteConfig(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    @ApiOperation("批量更新系统配置")
    @PutMapping("/batch")
    public Result<String> batchUpdateConfigs(@RequestBody List<SystemConfiguration> configs) {
        try {
            boolean success = systemConfigurationService.batchUpdateConfigs(configs);
            if (success) {
                return Result.success("批量更新成功");
            }
            return Result.error("批量更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
