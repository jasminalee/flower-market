package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vtc.xueqing.flower.entity.KnowledgeComment;

/**
 * Knowledge Comment Service
 */
public interface KnowledgeCommentService extends IService<KnowledgeComment> {
    
    /**
     * Get comments for knowledge article
     */
    IPage<KnowledgeComment> getCommentsPage(Page<KnowledgeComment> page, Long knowledgeId);
    
    /**
     * Submit new comment
     */
    KnowledgeComment addComment(KnowledgeComment comment);
}
