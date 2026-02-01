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
 * System Configuration Controller
 */
@Api(tags = "System Configuration Management")
@RestController
@RequestMapping("/api/admin/config")
public class SystemConfigurationController {
    
    @Autowired
    private SystemConfigurationService systemConfigurationService;
    
    @ApiOperation("Get System Configuration List (Pagination)")
    @GetMapping
    public Result<Page<SystemConfiguration>> getConfigList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category) {
        Page<SystemConfiguration> page = systemConfigurationService.getConfigList(pageNum, pageSize, category);
        return Result.success(page);
    }
    
    @ApiOperation("Get Configuration by Key")
    @GetMapping("/key/{configKey}")
    public Result<SystemConfiguration> getByKey(@PathVariable String configKey) {
        SystemConfiguration config = systemConfigurationService.getByKey(configKey);
        if (config == null) {
            return Result.error("Configuration does not exist");
        }
        return Result.success(config);
    }
    
    @ApiOperation("Get Configuration List by Category")
    @GetMapping("/category/{category}")
    public Result<List<SystemConfiguration>> getByCategory(@PathVariable String category) {
        List<SystemConfiguration> configs = systemConfigurationService.getByCategory(category);
        return Result.success(configs);
    }
    
    @ApiOperation("Create System Configuration")
    @PostMapping
    public Result<String> createConfig(@RequestBody SystemConfiguration config) {
        try {
            boolean success = systemConfigurationService.createConfig(config);
            if (success) {
                return Result.success("Creation Successful");
            }
            return Result.error("Creation Failed");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update System Configuration")
    @PutMapping("/{id}")
    public Result<String> updateConfig(@PathVariable Long id, @RequestBody SystemConfiguration config) {
        try {
            config.setId(id);
            boolean success = systemConfigurationService.updateConfig(config);
            if (success) {
                return Result.success("Update Successful");
            }
            return Result.error("Update Failed");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Delete System Configuration")
    @DeleteMapping("/{id}")
    public Result<String> deleteConfig(@PathVariable Long id) {
        boolean success = systemConfigurationService.deleteConfig(id);
        if (success) {
            return Result.success("Deletion Successful");
        }
        return Result.error("Deletion Failed");
    }
    
    @ApiOperation("Batch Update System Configuration")
    @PutMapping("/batch")
    public Result<String> batchUpdateConfigs(@RequestBody List<SystemConfiguration> configs) {
        try {
            boolean success = systemConfigurationService.batchUpdateConfigs(configs);
            if (success) {
                return Result.success("Batch Update Successful");
            }
            return Result.error("Batch Update Failed");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
