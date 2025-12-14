package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.ProductCategory;
import vtc.xueqing.flower.service.ProductCategoryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员-分类管理Controller
 */
@Slf4j
@Api(tags = "管理员-分类管理接口")
@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    @ApiOperation("获取所有分类列表")
    @GetMapping
    public Result<List<ProductCategory>> getAllCategories() {
        log.info("管理员获取所有分类列表");
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        return Result.success(categories);
    }

    @ApiOperation("根据ID获取分类详情")
    @GetMapping("/{id}")
    public Result<ProductCategory> getCategoryById(
            @ApiParam("分类ID") @PathVariable Long id
    ) {
        log.info("管理员获取分类详情: {}", id);
        ProductCategory category = productCategoryService.getCategoryById(id);
        return Result.success(category);
    }

    @ApiOperation("创建分类")
    @PostMapping
    public Result<ProductCategory> createCategory(
            @ApiParam("分类信息") @RequestBody ProductCategory category
    ) {
        log.info("管理员创建分类: {}", category);
        ProductCategory created = productCategoryService.createCategory(category);
        return Result.success(created);
    }

    @ApiOperation("更新分类")
    @PutMapping("/{id}")
    public Result<ProductCategory> updateCategory(
            @ApiParam("分类ID") @PathVariable Long id,
            @ApiParam("分类信息") @RequestBody ProductCategory category
    ) {
        log.info("管理员更新分类: {} - {}", id, category);
        ProductCategory updated = productCategoryService.updateCategory(id, category);
        return Result.success(updated);
    }

    @ApiOperation("删除分类（级联删除子分类）")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @ApiParam("分类ID") @PathVariable Long id
    ) {
        log.info("管理员删除分类: {}", id);
        productCategoryService.deleteCategory(id);
        return Result.success(null);
    }
}
