package vtc.xueqing.flower.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import vtc.xueqing.flower.common.Result;
import vtc.xueqing.flower.entity.Supplier;
import vtc.xueqing.flower.service.SupplierService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Admin Supplier Controller
 * For administrator to manage suppliers
 */
@Api(tags = "Admin Supplier")
@RestController
@RequestMapping("/api/admin/suppliers")
public class AdminSupplierController {

    @Resource
    private SupplierService supplierService;

    @ApiOperation("Get Supplier List (Pagination)")
    @GetMapping("/page")
    public Result<Page<Supplier>> getSupplierPage(
            @ApiParam("Current Page") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("Page Size") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("Supplier Name") @RequestParam(required = false) String name,
            @ApiParam("Status") @RequestParam(required = false) String status
    ) {
        Page<Supplier> page = new Page<>(current, size);
        return Result.success(supplierService.getSuppliersByPage(page, name, status));
    }

    @ApiOperation("Get All Active Suppliers")
    @GetMapping("/list")
    public Result<List<Supplier>> getActiveSuppliers() {
        return Result.success(supplierService.lambdaQuery()
                .eq(Supplier::getStatus, "ACTIVE")
                .list());
    }

    @ApiOperation("Get Supplier Details")
    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable Long id) {
        return Result.success(supplierService.getById(id));
    }

    @ApiOperation("Save Supplier (Add/Update)")
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Supplier supplier) {
        return Result.success(supplierService.saveOrUpdate(supplier));
    }

    @ApiOperation("Delete Supplier")
    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        return Result.success(supplierService.removeById(id));
    }

    @ApiOperation("Update Supplier Status")
    @PutMapping("/{id}/status/{status}")
    public Result<Boolean> updateStatus(@PathVariable Long id, @PathVariable String status) {
        return Result.success(supplierService.lambdaUpdate()
                .set(Supplier::getStatus, status)
                .eq(Supplier::getId, id)
                .update());
    }
}
