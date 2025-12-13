package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Product;

/**
 * 产品服务接口
 */
public interface ProductService {

    /**
     * 创建产品
     * @param product 产品信息
     * @return 创建成功的产品信息
     */
    Product createProduct(Product product);

    /**
     * 更新产品
     * @param product 产品信息
     * @return 更新成功的产品信息
     */
    Product updateProduct(Product product);

    /**
     * 删除产品（逻辑删除）
     * @param prodId 产品ID
     * @return 是否成功
     */
    boolean deleteProduct(Long prodId);

    /**
     * 根据ID获取产品详情
     * @param prodId 产品ID
     * @return 产品信息
     */
    Product getProductById(Long prodId);

    /**
     * 分页查询产品列表
     * @param current 当前页
     * @param size 每页大小
     * @param catId 分类ID（可选）
     * @param merchId 商家ID（可选）
     * @param keyword 关键词（可选）
     * @return 分页结果
     */
    Page<Product> getProductPage(Long current, Long size, Long catId, Long merchId, String keyword);

    /**
     * 更新产品状态（上架/下架）
     * @param prodId 产品ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateProductStatus(Long prodId, String status);
}
