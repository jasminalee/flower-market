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
 * Care Knowledge Service Implementation Class
 */
@Service
public class CareKnowledgeServiceImpl implements CareKnowledgeService {
    
    @Resource
    private CareKnowledgeMapper careKnowledgeMapper;
    
    @Override
    public IPage<CareKnowledge> getCareKnowledgePage(Page<CareKnowledge> page, String category, String status, String keyword) {
        LambdaQueryWrapper<CareKnowledge> wrapper = new LambdaQueryWrapper<>();
        
        // Filter conditions
        wrapper.eq(category != null && !category.isEmpty(), CareKnowledge::getCategory, category)
                .eq(status != null && !status.isEmpty(), CareKnowledge::getStatus, status);
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(CareKnowledge::getTitle, keyword)
                    .or()
                    .like(CareKnowledge::getContent, keyword));
        }
        
        wrapper.orderByDesc(CareKnowledge::getCreateDate);
        
        return careKnowledgeMapper.selectPage(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge getCareKnowledgeById(Long id) {
        CareKnowledge careKnowledge = careKnowledgeMapper.selectById(id);
        if (careKnowledge == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        
        // Increase view count
        careKnowledge.setViewCount(careKnowledge.getViewCount() + 1);
        careKnowledgeMapper.updateById(careKnowledge);
        
        return careKnowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge createCareKnowledge(CareKnowledge careKnowledge) {
        // Set default status
        if (careKnowledge.getStatus() == null || careKnowledge.getStatus().isEmpty()) {
            careKnowledge.setStatus("PUBLISHED");
        }
        
        // Initialize view count
        if (careKnowledge.getViewCount() == null) {
            careKnowledge.setViewCount(0);
        }
        
        // Validate required fields
        if (careKnowledge.getTitle() == null || careKnowledge.getTitle().isEmpty()) {
            throw new RuntimeException("Title cannot be empty");
        }
        
        if (careKnowledge.getContent() == null || careKnowledge.getContent().isEmpty()) {
            throw new RuntimeException("Content cannot be empty");
        }
        
        careKnowledgeMapper.insert(careKnowledge);
        return careKnowledge;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CareKnowledge updateCareKnowledge(CareKnowledge careKnowledge) {
        // Check if exists
        CareKnowledge existing = careKnowledgeMapper.selectById(careKnowledge.getId());
        if (existing == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        
        // Validate required fields
        if (careKnowledge.getTitle() != null && careKnowledge.getTitle().isEmpty()) {
            throw new RuntimeException("Title cannot be empty");
        }
        
        if (careKnowledge.getContent() != null && careKnowledge.getContent().isEmpty()) {
            throw new RuntimeException("Content cannot be empty");
        }
        
        careKnowledgeMapper.updateById(careKnowledge);
        return careKnowledgeMapper.selectById(careKnowledge.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCareKnowledge(Long id) {
        CareKnowledge careKnowledge = careKnowledgeMapper.selectById(id);
        if (careKnowledge == null) {
            throw new RuntimeException("Care knowledge does not exist");
        }
        
        careKnowledgeMapper.deleteById(id);
    }
}
