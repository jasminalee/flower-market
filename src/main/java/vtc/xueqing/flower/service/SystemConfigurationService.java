package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.SystemConfiguration;

import java.util.List;

/**
 * 系统配置服务接口
 */
public interface SystemConfigurationService {
    
    /**
     * 获取配置列表（分页）
     */
    Page<SystemConfiguration> getConfigList(Integer pageNum, Integer pageSize, String category);
    
    /**
     * 根据配置键获取配置
     */
    SystemConfiguration getByKey(String configKey);
    
    /**
     * 根据分类获取配置列表
     */
    List<SystemConfiguration> getByCategory(String category);
    
    /**
     * 创建配置
     */
    boolean createConfig(SystemConfiguration config);
    
    /**
     * 更新配置
     */
    boolean updateConfig(SystemConfiguration config);
    
    /**
     * 删除配置
     */
    boolean deleteConfig(Long id);
    
    /**
     * 批量更新配置
     */
    boolean batchUpdateConfigs(List<SystemConfiguration> configs);
}
