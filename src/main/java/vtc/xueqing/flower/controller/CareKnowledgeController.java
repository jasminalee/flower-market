package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.CareKnowledge;
import vtc.xueqing.flower.service.CareKnowledgeService;

import javax.annotation.Resource;

/**
 * 养护知识控制器
 */
@Api(tags = "养护知识管理")
@RestController
@RequestMapping("/api/care-knowledge")
public class CareKnowledgeController {
    
    @Resource
    private CareKnowledgeService careKnowledgeService;
    
    @ApiOperation("获取养护知识列表（分页）")
    @GetMapping
    public Result<IPage<CareKnowledge>> getCareKnowledgeList(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("分类") @RequestParam(required = false) String category,
            @ApiParam("状态") @RequestParam(required = false) String status
    ) {
        try {
            Page<CareKnowledge> page = new Page<>(current, size);
            IPage<CareKnowledge> knowledgePage = careKnowledgeService.getCareKnowledgePage(page, category, status);
            return Result.success(knowledgePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("获取养护知识详情")
    @GetMapping("/{id}")
    public Result<CareKnowledge> getCareKnowledgeById(@PathVariable("id") Long id) {
        try {
            CareKnowledge careKnowledge = careKnowledgeService.getCareKnowledgeById(id);
            return Result.success(careKnowledge);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("发布养护知识（管理员）")
    @PostMapping
    public Result<CareKnowledge> createCareKnowledge(@RequestBody CareKnowledge careKnowledge) {
        try {
            CareKnowledge created = careKnowledgeService.createCareKnowledge(careKnowledge);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("更新养护知识（管理员）")
    @PutMapping("/{id}")
    public Result<CareKnowledge> updateCareKnowledge(
            @PathVariable("id") Long id,
            @RequestBody CareKnowledge careKnowledge
    ) {
        try {
            careKnowledge.setId(id);
            CareKnowledge updated = careKnowledgeService.updateCareKnowledge(careKnowledge);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除养护知识（管理员）")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCareKnowledge(@PathVariable("id") Long id) {
        try {
            careKnowledgeService.deleteCareKnowledge(id);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
