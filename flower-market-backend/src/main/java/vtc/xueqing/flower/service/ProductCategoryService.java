package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.ProductCategory;

import java.util.List;

/**
 * Product category service interface.
 */
public interface ProductCategoryService {

    /**
     * Get category list (paginated).
     * @param current current page
     * @param size page size
     * @param parentId parent category ID (optional)
     * @return page result
     */
    Page<ProductCategory> getCategoryPage(Long current, Long size, Long parentId);

    /**
     * Get subcategories by parent ID.
     * @param parentId parent category ID
     * @return subcategory list
     */
    List<ProductCategory> getCategoriesByParentId(Long parentId);

    /**
     * Get category detail by ID.
     * @param cateId category ID
     * @return category info
     */
    ProductCategory getCategoryById(Long cateId);

    /**
     * Get all categories (unpaged).
     * @return all categories
     */
    List<ProductCategory> getAllCategories();

    /**
     * Create category.
     * @param category category info
     * @return created category
     */
    ProductCategory createCategory(ProductCategory category);

    /**
     * Update category.
     * @param cateId category ID
     * @param category category info
     * @return updated category
     */
    ProductCategory updateCategory(Long cateId, ProductCategory category);

    /**
     * Delete category (cascade delete children).
     * @param cateId category ID
     */
    void deleteCategory(Long cateId);
}
