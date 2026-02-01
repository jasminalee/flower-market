package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vtc.xueqing.flower.entity.Product;
import vtc.xueqing.flower.entity.ProductTrackability;
import vtc.xueqing.flower.mapper.ProductMapper;
import vtc.xueqing.flower.mapper.ProductTrackabilityMapper;
import vtc.xueqing.flower.service.ProductTrackabilityService;

import javax.annotation.Resource;

/**
 * Product traceability service implementation.
 */
@Service
public class ProductTrackabilityServiceImpl implements ProductTrackabilityService {
    
    @Resource
    private ProductTrackabilityMapper productTrackabilityMapper;
    
    @Resource
    private ProductMapper productMapper;
    
    @Override
    public ProductTrackability getByProductId(Long prodId) {
        LambdaQueryWrapper<ProductTrackability> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductTrackability::getProdId, prodId);
        
        ProductTrackability trackability = productTrackabilityMapper.selectOne(wrapper);
        if (trackability == null) {
            throw new RuntimeException("No traceability information available for this product");
        }
        
        return trackability;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductTrackability saveOrUpdate(ProductTrackability productTrackability) {
        // 1. Check whether the product exists
        Product product = productMapper.selectById(productTrackability.getProdId());
        if (product == null) {
            throw new RuntimeException("Product does not exist");
        }
        
        // 2. Check whether traceability info already exists
        LambdaQueryWrapper<ProductTrackability> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductTrackability::getProdId, productTrackability.getProdId());
        ProductTrackability existing = productTrackabilityMapper.selectOne(wrapper);
        
        if (existing != null) {
            // Update existing record
            productTrackability.setId(existing.getId());
            productTrackabilityMapper.updateById(productTrackability);
        } else {
            // Insert new record
            productTrackabilityMapper.insert(productTrackability);
        }
        
        return productTrackability;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProductId(Long prodId) {
        LambdaQueryWrapper<ProductTrackability> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductTrackability::getProdId, prodId);
        
        int deleted = productTrackabilityMapper.delete(wrapper);
        if (deleted == 0) {
            throw new RuntimeException("Traceability record does not exist");
        }
    }
}
