package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vtc.xueqing.flower.entity.ProductCategory;
import vtc.xueqing.flower.exception.BusinessException;
import vtc.xueqing.flower.mapper.ProductCategoryMapper;
import vtc.xueqing.flower.service.ProductCategoryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品分类服务实现类
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<ProductCategory> getAllCategories() {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSortOrder);
        return productCategoryMapper.selectList(wrapper);
    }

    @Override
    public List<ProductCategory> getCategoriesByParentId(Long parentId) {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductCategory::getParentId, parentId);
        wrapper.orderByAsc(ProductCategory::getSortOrder);
        return productCategoryMapper.selectList(wrapper);
    }

    @Override
    public ProductCategory getCategoryById(Long cateId) {
        ProductCategory category = productCategoryMapper.selectById(cateId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        return category;
    }
}
