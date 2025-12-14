package vtc.xueqing.flower.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public Page<ProductCategory> getCategoryPage(Long current, Long size, Long parentId) {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        if (parentId != null) {
            wrapper.eq(ProductCategory::getParentId, parentId);
        }
        wrapper.orderByAsc(ProductCategory::getSortOrder);

        Page<ProductCategory> page = new Page<>(current, size);
        return productCategoryMapper.selectPage(page, wrapper);
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

    @Override
    public List<ProductCategory> getAllCategories() {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProductCategory::getSortOrder);
        return productCategoryMapper.selectList(wrapper);
    }

    @Override
    public ProductCategory createCategory(ProductCategory category) {
        // 验证父分类是否存在
        if (category.getParentId() != null && category.getParentId() != 0) {
            ProductCategory parent = productCategoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new BusinessException("父分类不存在");
            }
        } else {
            category.setParentId(0L);
        }

        // 设置默认值
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        productCategoryMapper.insert(category);
        return category;
    }

    @Override
    public ProductCategory updateCategory(Long cateId, ProductCategory category) {
        // 检查分类是否存在
        ProductCategory existCategory = productCategoryMapper.selectById(cateId);
        if (existCategory == null) {
            throw new BusinessException("分类不存在");
        }

        // 验证父分类
        if (category.getParentId() != null && category.getParentId() != 0) {
            if (category.getParentId().equals(cateId)) {
                throw new BusinessException("不能将自己设置为父分类");
            }
            ProductCategory parent = productCategoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new BusinessException("父分类不存在");
            }
        }

        // 更新分类
        category.setCateId(cateId);
        productCategoryMapper.updateById(category);
        return productCategoryMapper.selectById(cateId);
    }

    @Override
    public void deleteCategory(Long cateId) {
        // 检查分类是否存在
        ProductCategory category = productCategoryMapper.selectById(cateId);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 递归删除所有子分类
        deleteChildCategories(cateId);

        // 删除当前分类
        productCategoryMapper.deleteById(cateId);
        log.info("删除分类: {}", cateId);
    }

    /**
     * 递归删除子分类
     */
    private void deleteChildCategories(Long parentId) {
        List<ProductCategory> children = getCategoriesByParentId(parentId);
        for (ProductCategory child : children) {
            deleteChildCategories(child.getCateId());
            productCategoryMapper.deleteById(child.getCateId());
        }
    }
}
