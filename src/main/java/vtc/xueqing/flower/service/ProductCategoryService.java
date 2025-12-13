package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.ProductCategory;

import java.util.List;

/**
 * 产品分类服务接口
 */
public interface ProductCategoryService {

    /**
     * 获取所有分类列表
     * @return 分类列表
     */
    List<ProductCategory> getAllCategories();

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
}
