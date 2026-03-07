package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.KnowledgeComment;
import vtc.xueqing.flower.service.KnowledgeCommentService;

import javax.annotation.Resource;

/**
 * Knowledge Comment Controller
 */
@Api(tags = "Knowledge Comment Management")
@RestController
@RequestMapping("/api/knowledge-comments")
public class KnowledgeCommentController {
    
    @Resource
    private KnowledgeCommentService knowledgeCommentService;
    
    @ApiOperation("Get comments list")
    @GetMapping("/{knowledgeId}")
    public Result<IPage<KnowledgeComment>> getComments(
            @PathVariable("knowledgeId") Long knowledgeId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<KnowledgeComment> page = new Page<>(current, size);
        IPage<KnowledgeComment> commentPage = knowledgeCommentService.getCommentsPage(page, knowledgeId);
        return Result.success(commentPage);
    }
    
    @ApiOperation("Submit comment")
    @PostMapping
    public Result<KnowledgeComment> addComment(@RequestBody KnowledgeComment comment) {
        try {
            KnowledgeComment created = knowledgeCommentService.addComment(comment);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
