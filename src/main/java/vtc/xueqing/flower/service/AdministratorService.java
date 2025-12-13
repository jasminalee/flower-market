package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Administrator;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.entity.Merchant;

/**
 * 管理员服务接口
 */
public interface AdministratorService {
    
    /**
     * 管理员登录
     * @param email 邮箱
     * @param password 密码
     * @return 管理员信息
     */
    Administrator login(String email, String password);
    
    /**
     * 获取顾客列表（分页）
     * @param page 分页信息
     * @param level 会员等级（可选）
     * @return 顾客列表
     */
    IPage<Customer> getCustomerList(Page<Customer> page, String level);
    
    /**
     * 获取商家列表（分页）
     * @param page 分页信息
     * @param status 商家状态（可选）
     * @return 商家列表
     */
    IPage<Merchant> getMerchantList(Page<Merchant> page, String status);
    
    /**
     * 商家审核
     * @param merchId 商家ID
     * @param status 审核状态：ACTIVE-通过，REJECTED-拒绝，SUSPENDED-暂停
     * @return 更新后的商家信息
     */
    Merchant verifyMerchant(Long merchId, String status);
    
    /**
     * 更新顾客会员等级
     * @param userId 用户ID
     * @param level 会员等级：NORMAL, VIP, SVIP
     * @return 更新后的顾客信息
     */
    Customer updateCustomerLevel(Long userId, String level);
}
