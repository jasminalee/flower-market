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
 * Administrator - Category Management Controller
 */
@Slf4j
@Api(tags = "Administrator - Category Management Interface")
@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    @ApiOperation("Get All Categories List")
    @GetMapping
    public Result<List<ProductCategory>> getAllCategories() {
        log.info("Administrator getting all categories list");
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        return Result.success(categories);
    }

    @ApiOperation("Get Category Details by ID")
    @GetMapping("/{id}")
    public Result<ProductCategory> getCategoryById(
            @ApiParam("Category ID") @PathVariable Long id
    ) {
        log.info("Administrator getting category details: {}", id);
        ProductCategory category = productCategoryService.getCategoryById(id);
        return Result.success(category);
    }

    @ApiOperation("Create Category")
    @PostMapping
    public Result<ProductCategory> createCategory(
            @ApiParam("Category Information") @RequestBody ProductCategory category
    ) {
        log.info("Administrator creating category: {}", category);
        ProductCategory created = productCategoryService.createCategory(category);
        return Result.success(created);
    }

    @ApiOperation("Update Category")
    @PutMapping("/{id}")
    public Result<ProductCategory> updateCategory(
            @ApiParam("Category ID") @PathVariable Long id,
            @ApiParam("Category Information") @RequestBody ProductCategory category
    ) {
        log.info("Administrator updating category: {} - {}", id, category);
        ProductCategory updated = productCategoryService.updateCategory(id, category);
        return Result.success(updated);
    }

    @ApiOperation("Delete Category (Cascade Delete Subcategories)")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @ApiParam("Category ID") @PathVariable Long id
    ) {
        log.info("Administrator deleting category: {}", id);
        productCategoryService.deleteCategory(id);
        return Result.success(null);
    }
}
