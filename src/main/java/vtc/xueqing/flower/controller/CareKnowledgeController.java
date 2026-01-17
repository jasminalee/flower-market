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
 * Care Knowledge Controller
 */
@Api(tags = "Care Knowledge Management")
@RestController
@RequestMapping("/api/care-knowledge")
public class CareKnowledgeController {
    
    @Resource
    private CareKnowledgeService careKnowledgeService;
    
    @ApiOperation("Get Care Knowledge List (Pagination)")
    @GetMapping
    public Result<IPage<CareKnowledge>> getCareKnowledgeList(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Category") @RequestParam(required = false) String category,
            @ApiParam("Status") @RequestParam(required = false) String status
    ) {
        try {
            Page<CareKnowledge> page = new Page<>(current, size);
            IPage<CareKnowledge> knowledgePage = careKnowledgeService.getCareKnowledgePage(page, category, status);
            return Result.success(knowledgePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Get Care Knowledge Details")
    @GetMapping("/{id}")
    public Result<CareKnowledge> getCareKnowledgeById(@PathVariable("id") Long id) {
        try {
            CareKnowledge careKnowledge = careKnowledgeService.getCareKnowledgeById(id);
            return Result.success(careKnowledge);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Publish Care Knowledge (Admin)")
    @PostMapping
    public Result<CareKnowledge> createCareKnowledge(@RequestBody CareKnowledge careKnowledge) {
        try {
            CareKnowledge created = careKnowledgeService.createCareKnowledge(careKnowledge);
            return Result.success(created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Update Care Knowledge (Admin)")
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
    
    @ApiOperation("Delete Care Knowledge (Admin)")
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
