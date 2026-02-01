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
 * Product Controller
 */
@Slf4j
@Api(tags = "Product Management Interface")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @ApiOperation("Create Product")
    @PostMapping
    public Result<Product> createProduct(@Validated @RequestBody Product product) {
        Product result = productService.createProduct(product);
        return Result.success("Creation Successful", result);
    }

    @ApiOperation("Update Product")
    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @Validated @RequestBody Product product) {
        product.setProdId(id);
        Product result = productService.updateProduct(product);
        return Result.success("Update Successful", result);
    }

    @ApiOperation("Delete Product")
    @DeleteMapping("/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        boolean result = productService.deleteProduct(id);
        return result ? Result.success("Deletion Successful") : Result.error("Deletion Failed");
    }

    @ApiOperation("Get Product Details")
    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return Result.success(product);
    }

    @ApiOperation("Query Product List with Pagination")
    @GetMapping
    public Result<Page<Product>> getProductPage(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("Category ID") @RequestParam(required = false) Long catId,
            @ApiParam("Merchant ID") @RequestParam(required = false) Long merchId,
            @ApiParam("Keyword") @RequestParam(required = false) String keyword
    ) {
        Page<Product> page = productService.getProductPage(current, size, catId, merchId, keyword);
        return Result.success(page);
    }

    @ApiOperation("Publish Product")
    @PutMapping("/{id}/publish")
    public Result<String> publishProduct(@PathVariable Long id) {
        boolean result = productService.updateProductStatus(id, Constants.PRODUCT_STATUS_ACTIVE);
        return result ? Result.success("Listing Successful") : Result.error("Listing Failed");
    }

    @ApiOperation("Unpublish Product")
    @PutMapping("/{id}/unpublish")
    public Result<String> unpublishProduct(@PathVariable Long id) {
        boolean result = productService.updateProductStatus(id, Constants.PRODUCT_STATUS_INACTIVE);
        return result ? Result.success("Delisting Successful") : Result.error("Delisting Failed");
    }
}
