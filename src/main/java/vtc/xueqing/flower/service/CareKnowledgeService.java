package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.CareKnowledge;

/**
 * 养护知识服务接口
 */
public interface CareKnowledgeService {
    
    /**
     * 获取养护知识列表（分页）
     * @param page 分页信息
     * @param category 分类（可选）
     * @param status 状态（可选）
     * @return 养护知识列表
     */
    IPage<CareKnowledge> getCareKnowledgePage(Page<CareKnowledge> page, String category, String status);
    
    /**
     * 根据ID获取养护知识详情
     * @param id 知识ID
     * @return 养护知识详情
     */
    CareKnowledge getCareKnowledgeById(Long id);
    
    /**
     * 发布养护知识（管理员）
     * @param careKnowledge 养护知识信息
     * @return 创建的养护知识
     */
    CareKnowledge createCareKnowledge(CareKnowledge careKnowledge);
    
    /**
     * 更新养护知识（管理员）
     * @param careKnowledge 养护知识信息
     * @return 更新后的养护知识
     */
    CareKnowledge updateCareKnowledge(CareKnowledge careKnowledge);
    
    /**
     * 删除养护知识（管理员）
     * @param id 知识ID
     */
    void deleteCareKnowledge(Long id);
}
