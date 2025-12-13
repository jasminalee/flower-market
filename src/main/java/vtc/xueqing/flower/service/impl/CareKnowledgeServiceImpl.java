package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.CareKnowledge;
import vtc.xueqing.flower.mapper.CareKnowledgeMapper;
import vtc.xueqing.flower.service.CareKnowledgeService;

import javax.annotation.Resource;

/**
 * 养护知识服务实现类
 */
@Service
public class CareKnowledgeServiceImpl implements CareKnowledgeService {
    
    @Resource
    private CareKnowledgeMapper careKnowledgeMapper;
    
    @Override
    public IPage<CareKnowledge> getCareKnowledgePage(Page<CareKnowledge> page, String category, String status) {
        LambdaQueryWrapper<CareKnowledge> wrapper = new LambdaQueryWrapper<>();
        
        // 筛选条件
        wrapper.eq(category != null && !category.isEmpty(), CareKnowledge::getCategory, category)
                .eq(status != null && !status.isEmpty(), CareKnowledge::getStatus, status)
                .orderByDesc(CareKnowledge::getCreateDate);
        
        return careKnowledgeMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge getCareKnowledgeById(Long id) {
        CareKnowledge careKnowledge = careKnowledgeMapper.selectById(id);
        if (careKnowledge == null) {
            throw new RuntimeException("养护知识不存在");
        }
        
        // 增加浏览次数
        careKnowledge.setViewCount(careKnowledge.getViewCount() + 1);
        careKnowledgeMapper.updateById(careKnowledge);
        
        return careKnowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge createCareKnowledge(CareKnowledge careKnowledge) {
        // 设置默认状态
        if (careKnowledge.getStatus() == null || careKnowledge.getStatus().isEmpty()) {
            careKnowledge.setStatus("PUBLISHED");
        }
        
        // 初始化浏览次数
        if (careKnowledge.getViewCount() == null) {
            careKnowledge.setViewCount(0);
        }
        
        // 验证必填字段
        if (careKnowledge.getTitle() == null || careKnowledge.getTitle().isEmpty()) {
            throw new RuntimeException("标题不能为空");
        }
        
        if (careKnowledge.getContent() == null || careKnowledge.getContent().isEmpty()) {
            throw new RuntimeException("内容不能为空");
        }
        
        careKnowledgeMapper.insert(careKnowledge);
        return careKnowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge updateCareKnowledge(CareKnowledge careKnowledge) {
        // 检查是否存在
        CareKnowledge existing = careKnowledgeMapper.selectById(careKnowledge.getId());
        if (existing == null) {
            throw new RuntimeException("养护知识不存在");
        }
        
        // 验证必填字段
        if (careKnowledge.getTitle() != null && careKnowledge.getTitle().isEmpty()) {
            throw new RuntimeException("标题不能为空");
        }
        
        if (careKnowledge.getContent() != null && careKnowledge.getContent().isEmpty()) {
            throw new RuntimeException("内容不能为空");
        }
        
        careKnowledgeMapper.updateById(careKnowledge);
        return careKnowledgeMapper.selectById(careKnowledge.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCareKnowledge(Long id) {
        CareKnowledge careKnowledge = careKnowledgeMapper.selectById(id);
        if (careKnowledge == null) {
            throw new RuntimeException("养护知识不存在");
        }
        
        careKnowledgeMapper.deleteById(id);
    }
}
