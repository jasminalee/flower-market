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
 * 产品分类Controller
 */
@Slf4j
@Api(tags = "产品分类接口")
@RestController
@RequestMapping("/api/products/categories")
public class ProductCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    @ApiOperation("获取所有分类列表")
    @GetMapping
    public Result<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        return Result.success(categories);
    }

    @ApiOperation("根据父分类ID获取子分类")
    @GetMapping("/parent/{parentId}")
    public Result<List<ProductCategory>> getCategoriesByParentId(
            @ApiParam("父分类ID") @PathVariable Long parentId
    ) {
        List<ProductCategory> categories = productCategoryService.getCategoriesByParentId(parentId);
        return Result.success(categories);
    }

    @ApiOperation("获取分类详情")
    @GetMapping("/{id}")
    public Result<ProductCategory> getCategoryById(@PathVariable Long id) {
        ProductCategory category = productCategoryService.getCategoryById(id);
        return Result.success(category);
    }
}
