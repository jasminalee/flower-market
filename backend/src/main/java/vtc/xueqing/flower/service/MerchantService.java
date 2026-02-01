package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.Merchant;

/**
 * Merchant service interface.
 */
public interface MerchantService {

    /**
     * Merchant registration.
     * @param merchant registration info
     * @return registered merchant info
     */
    Merchant register(Merchant merchant);

    /**
     * Merchant login.
     * @param login login info
     * @return merchant info after login
     */
    Merchant login(Merchant login);

    /**
     * Get merchant by ID.
     * @param merchId merchant ID
     * @return merchant info
     */
    Merchant getMerchantById(Long merchId);

    /**
     * Update merchant info.
     * @param merchant merchant info
     * @return updated merchant info
     */
    Merchant updateMerchant(Merchant merchant);
    
    /**
     * Get merchant dashboard data.
     * @param merchId merchant ID
     * @return dashboard data
     */
    java.util.Map<String, Object> getDashboardData(Long merchId);
    
    /**
     * Get merchant product list.
     * @param merchId merchant ID
     * @param current current page
     * @param size page size
     * @param keyword search keyword
     * @return product page data
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> getMerchantProducts(
        Long merchId, Long current, Long size, String keyword);
    
    /**
     * Get merchant order list.
     * @param page page object
     * @param merchId merchant ID
     * @param status order status (optional)
     * @return order page data
     */
    com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Order> getMerchantOrders(
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Order> page,
        Long merchId, String status);
    
    /**
     * Get merchant coupon list.
     * @param page page object
     * @param merchId merchant ID
     * @param status coupon status (optional)
     * @return coupon page data
     */
    com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon> getMerchantCoupons(
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Coupon> page,
        Long merchId, String status);
    
    /**
     * Get merchant coupon detail by ID.
     * @param id coupon ID
     * @return coupon info
     */
    vtc.xueqing.flower.entity.Coupon getMerchantCouponById(Long id);
    
    /**
     * Create merchant coupon.
     * @param coupon coupon info
     * @return created coupon
     */
    vtc.xueqing.flower.entity.Coupon createMerchantCoupon(vtc.xueqing.flower.entity.Coupon coupon);
    
    /**
     * Update merchant coupon.
     * @param coupon coupon info
     * @return updated coupon
     */
    vtc.xueqing.flower.entity.Coupon updateMerchantCoupon(vtc.xueqing.flower.entity.Coupon coupon);
    
    /**
     * Delete merchant coupon.
     * @param id coupon ID
     */
    void deleteMerchantCoupon(Long id);
    
    /**
     * Create a new product for merchant.
     * @param product product info
     * @return created product
     */
    vtc.xueqing.flower.entity.Product createProduct(vtc.xueqing.flower.entity.Product product);
}
