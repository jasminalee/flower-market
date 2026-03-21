package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Product;

/**
 * Product service interface.
 */
public interface ProductService {

    /**
     * Create product.
     * @param product product info
     * @return created product
     */
    Product createProduct(Product product);

    /**
     * Update product.
     * @param product product info
     * @return updated product
     */
    Product updateProduct(Product product);

    /**
     * Delete product (logical delete).
     * @param prodId product ID
     * @return whether success
     */
    boolean deleteProduct(Long prodId);

    /**
     * Get product detail by ID.
     * @param prodId product ID
     * @return product info
     */
    Product getProductById(Long prodId);

    /**
     * Paginated product list.
     * @param current current page
     * @param size page size
     * @param catId category ID (optional)
     * @param merchId merchant ID (optional)
     * @param keyword keyword (optional)
     * @param minPrice minimum price (optional)
     * @param maxPrice maximum price (optional)
     * @param status product status (optional)
     * @param sortBy sort field: price / sales (optional)
     * @param sortOrder sort direction: asc / desc (optional)
     * @return page result
     */
    Page<Product> getProductPage(Long current, Long size, Long catId, Long merchId, String keyword,
                                 java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice,
                                 String status, String sortBy, String sortOrder);

    /**
     * Update product status (publish/unpublish).
     * @param prodId product ID
     * @param status status
     * @return whether success
     */
    boolean updateProductStatus(Long prodId, String status);
}
