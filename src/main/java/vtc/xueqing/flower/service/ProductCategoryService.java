package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.ProductCategory;

import java.util.List;

/**
 * 产品分类服务接口
 */
public interface ProductCategoryService {

    /**
     * 分页获取分类列表
     * @param current 当前页
     * @param size 每页大小
     * @param parentId 父分类ID（可选）
     * @return 分页结果
     */
    Page<ProductCategory> getCategoryPage(Long current, Long size, Long parentId);

    /**
     * 根据父分类ID获取子分类列表
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<ProductCategory> getCategoriesByParentId(Long parentId);

    /**
     * 根据ID获取分类详情
     * @param cateId 分类ID
     * @return 分类信息
     */
    ProductCategory getCategoryById(Long cateId);

    /**
     * 获取所有分类列表（不分页）
     * @return 所有分类列表
     */
    List<ProductCategory> getAllCategories();

    /**
     * 创建分类
     * @param category 分类信息
     * @return 创建的分类
     */
    ProductCategory createCategory(ProductCategory category);

    /**
     * 更新分类
     * @param cateId 分类ID
     * @param category 分类信息
     * @return 更新后的分类
     */
    ProductCategory updateCategory(Long cateId, ProductCategory category);

    /**
     * 删除分类（级联删除子分类）
     * @param cateId 分类ID
     */
    void deleteCategory(Long cateId);
}
