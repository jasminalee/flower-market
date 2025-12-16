package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.Merchant;

/**
 * 商家服务接口
 */
public interface MerchantService {

    /**
     * 商家注册
     * @param merchant 注册信息
     * @return 注册成功的商家信息
     */
    Merchant register(Merchant merchant);

    /**
     * 商家登录
     * @param login 登录信息
     * @return 登录成功的商家信息
     */
    Merchant login(Merchant login);

    /**
     * 根据ID获取商家信息
     * @param merchId 商家ID
     * @return 商家信息
     */
    Merchant getMerchantById(Long merchId);

    /**
     * 更新商家信息
     * @param merchant 商家信息
     * @return 更新后的商家信息
     */
    Merchant updateMerchant(Merchant merchant);
    
    /**
     * 获取商家工作台数据
     * @param merchId 商家ID
     * @return 工作台数据
     */
    java.util.Map<String, Object> getDashboardData(Long merchId);
    
    /**
     * 获取商家商品列表
     * @param merchId 商家ID
     * @param current 当前页
     * @param size 每页大小
     * @param keyword 搜索关键词
     * @return 商品分页数据
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Product> getMerchantProducts(
        Long merchId, Long current, Long size, String keyword);
    
    /**
     * 获取商家订单列表
     * @param page 分页对象
     * @param merchId 商家ID
     * @param status 订单状态（可选）
     * @return 订单分页数据
     */
    com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Order> getMerchantOrders(
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Order> page,
        Long merchId, String status);
    
    /**
     * 获取商家优惠券列表
     * @param page 分页对象
     * @param merchId 商家ID
     * @param status 优惠券状态（可选）
     * @return 优惠券分页数据
     */
    com.baomidou.mybatisplus.core.metadata.IPage<vtc.xueqing.flower.entity.Coupon> getMerchantCoupons(
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<vtc.xueqing.flower.entity.Coupon> page,
        Long merchId, String status);
    
    /**
     * 根据ID获取优惠券详情
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    vtc.xueqing.flower.entity.Coupon getMerchantCouponById(Long id);
    
    /**
     * 创建商家优惠券
     * @param coupon 优惠券信息
     * @return 创建的优惠券
     */
    vtc.xueqing.flower.entity.Coupon createMerchantCoupon(vtc.xueqing.flower.entity.Coupon coupon);
    
    /**
     * 更新商家优惠券
     * @param coupon 优惠券信息
     * @return 更新后的优惠券
     */
    vtc.xueqing.flower.entity.Coupon updateMerchantCoupon(vtc.xueqing.flower.entity.Coupon coupon);
    
    /**
     * 删除商家优惠券
     * @param id 优惠券ID
     */
    void deleteMerchantCoupon(Long id);
}
