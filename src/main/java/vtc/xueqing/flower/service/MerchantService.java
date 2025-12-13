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
}
