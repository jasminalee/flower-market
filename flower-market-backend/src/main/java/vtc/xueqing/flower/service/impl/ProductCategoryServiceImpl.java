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
 * Product category service implementation.
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
            throw new BusinessException("Category does not exist");
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
        // Validate that the parent category exists
        if (category.getParentId() != null && category.getParentId() != 0) {
            ProductCategory parent = productCategoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new BusinessException("Parent category does not exist");
            }
        } else {
            category.setParentId(0L);
        }

        // Set default values
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        productCategoryMapper.insert(category);
        return category;
    }

    @Override
    public ProductCategory updateCategory(Long cateId, ProductCategory category) {
        // Check whether the category exists
        ProductCategory existCategory = productCategoryMapper.selectById(cateId);
        if (existCategory == null) {
            throw new BusinessException("Category does not exist");
        }

        // Validate parent category
        if (category.getParentId() != null && category.getParentId() != 0) {
            if (category.getParentId().equals(cateId)) {
                throw new BusinessException("Cannot set the category itself as its parent");
            }
            ProductCategory parent = productCategoryMapper.selectById(category.getParentId());
            if (parent == null) {
                throw new BusinessException("Parent category does not exist");
            }
        }

        // Update category
        category.setCateId(cateId);
        productCategoryMapper.updateById(category);
        return productCategoryMapper.selectById(cateId);
    }

    @Override
    public void deleteCategory(Long cateId) {
        // Check whether the category exists
        ProductCategory category = productCategoryMapper.selectById(cateId);
        if (category == null) {
            throw new BusinessException("Category does not exist");
        }

        // Recursively delete all child categories
        deleteChildCategories(cateId);

        // Delete current category
        productCategoryMapper.deleteById(cateId);
        log.info("Deleted category: {}", cateId);
    }

    /**
     * Recursively delete child categories.
     */
    private void deleteChildCategories(Long parentId) {
        List<ProductCategory> children = getCategoriesByParentId(parentId);
        for (ProductCategory child : children) {
            deleteChildCategories(child.getCateId());
            productCategoryMapper.deleteById(child.getCateId());
        }
    }
}
