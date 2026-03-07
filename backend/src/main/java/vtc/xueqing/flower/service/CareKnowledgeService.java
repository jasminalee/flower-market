package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.CareKnowledge;

/**
 * Care knowledge service interface.
 */
public interface CareKnowledgeService {
    
    /**
     * Get care knowledge list (paginated).
     * @param page pagination info
     * @param category category (optional)
     * @param status status (optional)
     * @param keyword keyword (optional)
     * @return care knowledge list
     */
    IPage<CareKnowledge> getCareKnowledgePage(Page<CareKnowledge> page, String category, String status, String keyword);
    
    /**
     * Get care knowledge detail by ID.
     * @param id knowledge ID
     * @return care knowledge detail
     */
    CareKnowledge getCareKnowledgeById(Long id);
    
    /**
     * Publish care knowledge (admin).
     * @param careKnowledge care knowledge info
     * @return created care knowledge
     */
    CareKnowledge createCareKnowledge(CareKnowledge careKnowledge);
    
    /**
     * Update care knowledge (admin).
     * @param careKnowledge care knowledge info
     * @return updated care knowledge
     */
    CareKnowledge updateCareKnowledge(CareKnowledge careKnowledge);
    
    /**
     * Delete care knowledge (admin).
     * @param id knowledge ID
     */
    void deleteCareKnowledge(Long id);
}
