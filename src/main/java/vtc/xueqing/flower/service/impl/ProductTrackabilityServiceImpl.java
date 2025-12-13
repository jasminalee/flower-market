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
 * 产品溯源服务实现类
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
            throw new RuntimeException("该产品暂无溯源信息");
        }
        
        return trackability;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductTrackability saveOrUpdate(ProductTrackability productTrackability) {
        // 1. 检查产品是否存在
        Product product = productMapper.selectById(productTrackability.getProdId());
        if (product == null) {
            throw new RuntimeException("产品不存在");
        }
        
        // 2. 检查是否已存在溯源信息
        LambdaQueryWrapper<ProductTrackability> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductTrackability::getProdId, productTrackability.getProdId());
        ProductTrackability existing = productTrackabilityMapper.selectOne(wrapper);
        
        if (existing != null) {
            // 更新
            productTrackability.setId(existing.getId());
            productTrackabilityMapper.updateById(productTrackability);
        } else {
            // 新增
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
            throw new RuntimeException("溯源信息不存在");
        }
    }
}
