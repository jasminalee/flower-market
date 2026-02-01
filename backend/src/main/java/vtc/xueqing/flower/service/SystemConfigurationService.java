package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.SystemConfiguration;

import java.util.List;

/**
 * System configuration service interface.
 */
public interface SystemConfigurationService {
    
    /**
     * Get configuration list (paginated).
     */
    Page<SystemConfiguration> getConfigList(Integer pageNum, Integer pageSize, String category);
    
    /**
     * Get configuration by key.
     */
    SystemConfiguration getByKey(String configKey);
    
    /**
     * Get configuration list by category.
     */
    List<SystemConfiguration> getByCategory(String category);
    
    /**
     * Create configuration.
     */
    boolean createConfig(SystemConfiguration config);
    
    /**
     * Update configuration.
     */
    boolean updateConfig(SystemConfiguration config);
    
    /**
     * Delete configuration.
     */
    boolean deleteConfig(Long id);
    
    /**
     * Batch update configurations.
     */
    boolean batchUpdateConfigs(List<SystemConfiguration> configs);
}
