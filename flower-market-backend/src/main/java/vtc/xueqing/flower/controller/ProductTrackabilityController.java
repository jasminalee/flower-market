package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.ProductTrackability;
import vtc.xueqing.flower.service.ProductTrackabilityService;

import javax.annotation.Resource;

/**
 * Product Trackability Controller
 */
@Api(tags = "Product Trackability Management")
@RestController
@RequestMapping("/api/products/trackability")
public class ProductTrackabilityController {
    
    @Resource
    private ProductTrackabilityService productTrackabilityService;
    
    @ApiOperation("Get Product Trackability Information")
    @GetMapping("/{prodId}")
    public Result<ProductTrackability> getByProductId(@PathVariable("prodId") Long prodId) {
        try {
            ProductTrackability trackability = productTrackabilityService.getByProductId(prodId);
            return Result.success(trackability);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Create or Update Product Trackability Information (Merchant)")
    @PostMapping
    public Result<ProductTrackability> saveOrUpdate(@RequestBody ProductTrackability productTrackability) {
        try {
            ProductTrackability saved = productTrackabilityService.saveOrUpdate(productTrackability);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("Delete Product Trackability Information")
    @DeleteMapping("/{prodId}")
    public Result<Void> deleteByProductId(@PathVariable("prodId") Long prodId) {
        try {
            productTrackabilityService.deleteByProductId(prodId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
