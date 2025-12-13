package vtc.xueqing.flower.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.ProductTrackability;
import vtc.xueqing.flower.service.ProductTrackabilityService;

import javax.annotation.Resource;

/**
 * 产品溯源控制器
 */
@Api(tags = "产品溯源管理")
@RestController
@RequestMapping("/api/products/trackability")
public class ProductTrackabilityController {
    
    @Resource
    private ProductTrackabilityService productTrackabilityService;
    
    @ApiOperation("获取产品溯源信息")
    @GetMapping("/{prodId}")
    public Result<ProductTrackability> getByProductId(@PathVariable("prodId") Long prodId) {
        try {
            ProductTrackability trackability = productTrackabilityService.getByProductId(prodId);
            return Result.success(trackability);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("创建或更新产品溯源信息（商家）")
    @PostMapping
    public Result<ProductTrackability> saveOrUpdate(@RequestBody ProductTrackability productTrackability) {
        try {
            ProductTrackability saved = productTrackabilityService.saveOrUpdate(productTrackability);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @ApiOperation("删除产品溯源信息")
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
