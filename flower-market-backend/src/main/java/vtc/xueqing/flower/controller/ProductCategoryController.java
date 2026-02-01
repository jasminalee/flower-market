package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * Product Category Controller
 */
@Slf4j
@Api(tags = "Product Category Interface")
@RestController
@RequestMapping("/api/products/categories")
public class ProductCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    @ApiOperation("Get Category List with Pagination")
    @GetMapping
    public Result<Page<ProductCategory>> getCategoryPage(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("Parent Category ID, Optional") @RequestParam(required = false) Long parentId
    ) {
        Page<ProductCategory> page = productCategoryService.getCategoryPage(current, size, parentId);
        return Result.success(page);
    }

    @ApiOperation("Get Child Categories by Parent Category ID")
    @GetMapping("/parent/{parentId}")
    public Result<List<ProductCategory>> getCategoriesByParentId(
            @ApiParam("Parent Category ID") @PathVariable Long parentId
    ) {
        List<ProductCategory> categories = productCategoryService.getCategoriesByParentId(parentId);
        return Result.success(categories);
    }

    @ApiOperation("Get Category Details")
    @GetMapping("/{id}")
    public Result<ProductCategory> getCategoryById(@PathVariable Long id) {
        ProductCategory category = productCategoryService.getCategoryById(id);
        return Result.success(category);
    }
    
    @ApiOperation("Get All Categories List")
    @GetMapping("/all")
    public Result<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        return Result.success(categories);
    }
}
