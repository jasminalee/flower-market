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
     * 获取顾客详情
     * @param userId 用户ID
     * @return 顾客信息
     */
    Customer getCustomerById(Long userId);
    
    /**
     * 获取商家列表（分页）
     * @param page 分页信息
     * @param status 商家状态（可选）
     * @return 商家列表
     */
    IPage<Merchant> getMerchantList(Page<Merchant> page, String status);
    
    /**
     * 获取商家详情
     * @param merchId 商家ID
     * @return 商家信息
     */
    Merchant getMerchantById(Long merchId);
    
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
    
    /**
     * 获取管理后台仪表板数据
     * @return 包含统计数据、最近用户、最近商家、订单趋势的Map
     */
    java.util.Map<String, Object> getDashboardData();
    
    /**
     * 获取所有订单列表（管理员）- 带客户和商家名称
     * @param page 分页信息
     * @param status 订单状态（可选）
     * @param keyword 搜索关键词：订单号或客户名（可选）
     * @return 订单VO列表
     */
    IPage<vtc.xueqing.flower.vo.OrderVO> getAllOrders(Page<vtc.xueqing.flower.vo.OrderVO> page, String status, String keyword);
    
    /**
     * 获取养护知识列表（管理员）
     * @param page 分页信息
     * @param keyword 搜索关键词：标题（可选）
     * @param category 分类（可选）
     * @param status 状态（可选）
     * @return 养护知识列表
     */
    IPage<vtc.xueqing.flower.entity.CareKnowledge> getKnowledgeList(Page<vtc.xueqing.flower.entity.CareKnowledge> page, String keyword, String category, String status);
    
    /**
     * 获取养护知识详情（管理员）
     * @param id 知识ID
     * @return 养护知识详情
     */
    vtc.xueqing.flower.entity.CareKnowledge getKnowledgeById(Long id);
    
    /**
     * 创建养护知识（管理员）
     * @param knowledge 养护知识信息
     * @return 创建的养护知识
     */
    vtc.xueqing.flower.entity.CareKnowledge createKnowledge(vtc.xueqing.flower.entity.CareKnowledge knowledge);
    
    /**
     * 更新养护知识（管理员）
     * @param knowledge 养护知识信息
     * @return 更新后的养护知识
     */
    vtc.xueqing.flower.entity.CareKnowledge updateKnowledge(vtc.xueqing.flower.entity.CareKnowledge knowledge);
    
    /**
     * 更新养护知识状态（管理员）
     * @param id 知识ID
     * @param status 状态
     * @return 更新后的养护知识
     */
    vtc.xueqing.flower.entity.CareKnowledge updateKnowledgeStatus(Long id, String status);
    
    /**
     * 删除养护知识（管理员）
     * @param id 知识ID
     */
    void deleteKnowledge(Long id);
    
    /**
     * 根据管理员ID获取个人信息
     * @param adminId 管理员ID
     * @return 管理员信息
     */
    Administrator getProfileById(Long adminId);
    
    /**
     * 更新管理员个人信息
     * @param adminId 管理员ID
     * @param name 姓名
     * @return 更新后的管理员信息
     */
    Administrator updateProfile(Long adminId, String name);
    
    /**
     * 修改管理员密码
     * @param adminId 管理员ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    void updatePassword(Long adminId, String oldPassword, String newPassword);
}
