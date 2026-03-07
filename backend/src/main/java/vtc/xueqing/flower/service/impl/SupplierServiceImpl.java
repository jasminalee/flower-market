package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vtc.xueqing.flower.entity.Supplier;
import vtc.xueqing.flower.mapper.SupplierMapper;
import vtc.xueqing.flower.service.SupplierService;

/**
 * Supplier Service implementation
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Override
    public Page<Supplier> getSuppliersByPage(Page<Supplier> page, String name, String status) {
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            queryWrapper.like(Supplier::getName, name);
        }
        
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(Supplier::getStatus, status);
        }
        
        queryWrapper.orderByDesc(Supplier::getCreateDate);
        
        return this.page(page, queryWrapper);
    }
}
