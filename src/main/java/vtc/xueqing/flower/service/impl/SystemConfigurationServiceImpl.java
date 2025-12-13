package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vtc.xueqing.flower.entity.SystemConfiguration;
import vtc.xueqing.flower.mapper.SystemConfigurationMapper;
import vtc.xueqing.flower.service.SystemConfigurationService;

import java.util.List;

/**
 * 系统配置服务实现类
 */
@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService {
    
    @Autowired
    private SystemConfigurationMapper systemConfigurationMapper;
    
    @Override
    public Page<SystemConfiguration> getConfigList(Integer pageNum, Integer pageSize, String category) {
        Page<SystemConfiguration> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SystemConfiguration> wrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(category)) {
            wrapper.eq("category", category);
        }
        
        wrapper.orderByDesc("create_date");
        return systemConfigurationMapper.selectPage(page, wrapper);
    }
    
    @Override
    public SystemConfiguration getByKey(String configKey) {
        QueryWrapper<SystemConfiguration> wrapper = new QueryWrapper<>();
        wrapper.eq("config_key", configKey);
        return systemConfigurationMapper.selectOne(wrapper);
    }
    
    @Override
    public List<SystemConfiguration> getByCategory(String category) {
        QueryWrapper<SystemConfiguration> wrapper = new QueryWrapper<>();
        wrapper.eq("category", category);
        wrapper.orderByAsc("config_key");
        return systemConfigurationMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createConfig(SystemConfiguration config) {
        // 检查配置键是否已存在
        QueryWrapper<SystemConfiguration> wrapper = new QueryWrapper<>();
        wrapper.eq("config_key", config.getConfigKey());
        Long count = systemConfigurationMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("配置键已存在: " + config.getConfigKey());
        }
        
        return systemConfigurationMapper.insert(config) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(SystemConfiguration config) {
        SystemConfiguration existing = systemConfigurationMapper.selectById(config.getId());
        if (existing == null) {
            throw new RuntimeException("配置不存在");
        }
        
        // 如果修改了configKey，检查新的key是否重复
        if (!existing.getConfigKey().equals(config.getConfigKey())) {
            QueryWrapper<SystemConfiguration> wrapper = new QueryWrapper<>();
            wrapper.eq("config_key", config.getConfigKey());
            wrapper.ne("id", config.getId());
            Long count = systemConfigurationMapper.selectCount(wrapper);
            if (count > 0) {
                throw new RuntimeException("配置键已存在: " + config.getConfigKey());
            }
        }
        
        return systemConfigurationMapper.updateById(config) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfig(Long id) {
        return systemConfigurationMapper.deleteById(id) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateConfigs(List<SystemConfiguration> configs) {
        for (SystemConfiguration config : configs) {
            if (config.getId() != null) {
                systemConfigurationMapper.updateById(config);
            }
        }
        return true;
    }
}
