package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vtc.xueqing.flower.entity.KnowledgeComment;
import vtc.xueqing.flower.mapper.KnowledgeCommentMapper;
import vtc.xueqing.flower.service.KnowledgeCommentService;

import java.time.LocalDateTime;

/**
 * Knowledge Comment Service Impl
 */
@Service
public class KnowledgeCommentServiceImpl extends ServiceImpl<KnowledgeCommentMapper, KnowledgeComment> implements KnowledgeCommentService {

    @Override
    public IPage<KnowledgeComment> getCommentsPage(Page<KnowledgeComment> page, Long knowledgeId) {
        LambdaQueryWrapper<KnowledgeComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KnowledgeComment::getKnowledgeId, knowledgeId)
                    .eq(KnowledgeComment::getStatus, "APPROVED")
                    .orderByDesc(KnowledgeComment::getCreateDate);
        return this.page(page, queryWrapper);
    }

    @Override
    public KnowledgeComment addComment(KnowledgeComment comment) {
        comment.setCreateDate(LocalDateTime.now());
        if (comment.getStatus() == null) {
            comment.setStatus("APPROVED"); // Default to approved for simple flow
        }
        this.save(comment);
        return comment;
    }
}
