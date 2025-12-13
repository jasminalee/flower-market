package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Constants;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.service.ProductService;

import javax.annotation.Resource;

/**
 * 产品Controller
 */
@Slf4j
@Api(tags = "产品管理接口")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @ApiOperation("创建产品")
    @PostMapping
    public Result<Product> createProduct(@Validated @RequestBody Product product) {
        Product result = productService.createProduct(product);
        return Result.success("创建成功", result);
    }

    @ApiOperation("更新产品")
    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @Validated @RequestBody Product product) {
        product.setProdId(id);
        Product result = productService.updateProduct(product);
        return Result.success("更新成功", result);
    }

    @ApiOperation("删除产品")
    @DeleteMapping("/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        boolean result = productService.deleteProduct(id);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }

    @ApiOperation("获取产品详情")
    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return Result.success(product);
    }

    @ApiOperation("分页查询产品列表")
    @GetMapping
    public Result<Page<Product>> getProductPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("分类ID") @RequestParam(required = false) Long catId,
            @ApiParam("商家ID") @RequestParam(required = false) Long merchId,
            @ApiParam("关键词") @RequestParam(required = false) String keyword
    ) {
        Page<Product> page = productService.getProductPage(current, size, catId, merchId, keyword);
        return Result.success(page);
    }

    @ApiOperation("上架产品")
    @PutMapping("/{id}/publish")
    public Result<String> publishProduct(@PathVariable Long id) {
        boolean result = productService.updateProductStatus(id, Constants.PRODUCT_STATUS_ACTIVE);
        return result ? Result.success("上架成功") : Result.error("上架失败");
    }

    @ApiOperation("下架产品")
    @PutMapping("/{id}/unpublish")
    public Result<String> unpublishProduct(@PathVariable Long id) {
        boolean result = productService.updateProductStatus(id, Constants.PRODUCT_STATUS_INACTIVE);
        return result ? Result.success("下架成功") : Result.error("下架失败");
    }
}
