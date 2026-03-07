package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vtc.xueqing.flower.entity.Supplier;

/**
 * Supplier Service interface
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * Get suppliers by name or status with pagination
     *
     * @param page   pagination info
     * @param name   supplier name (like)
     * @param status supplier status
     * @return paginated list
     */
    Page<Supplier> getSuppliersByPage(Page<Supplier> page, String name, String status);
}
