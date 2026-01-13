package vtc.xueqing.flower.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import vtc.xueqing.flower.entity.Administrator;
import vtc.xueqing.flower.entity.Customer;
import vtc.xueqing.flower.entity.Merchant;

/**
 * Administrator service interface.
 */
public interface AdministratorService {
    
    /**
     * Administrator login.
     * @param email email
     * @param password password
     * @return admin info
     */
    Administrator login(String email, String password);
    
    /**
     * Get customer list (paginated).
     * @param page pagination info
     * @param level membership level (optional)
     * @return customer list
     */
    IPage<Customer> getCustomerList(Page<Customer> page, String level);
    
    /**
     * Get customer detail.
     * @param userId user ID
     * @return customer info
     */
    Customer getCustomerById(Long userId);
    
    /**
     * Get merchant list (paginated).
     * @param page pagination info
     * @param status merchant status (optional)
     * @return merchant list
     */
    IPage<Merchant> getMerchantList(Page<Merchant> page, String status);
    
    /**
     * Get merchant detail.
     * @param merchId merchant ID
     * @return merchant info
     */
    Merchant getMerchantById(Long merchId);
    
    /**
     * Merchant review.
     * @param merchId merchant ID
     * @param status status: ACTIVE-approved, REJECTED-rejected, SUSPENDED-suspended
     * @return updated merchant info
     */
    Merchant verifyMerchant(Long merchId, String status);
    
    /**
     * Update customer membership level.
     * @param userId user ID
     * @param level membership level: NORMAL, VIP, SVIP
     * @return updated customer info
     */
    Customer updateCustomerLevel(Long userId, String level);
    
    /**
     * Get admin dashboard data.
     * @return map containing stats, recent users, recent merchants, order trends
     */
    java.util.Map<String, Object> getDashboardData();
    
    /**
     * Get all orders (admin) with customer and merchant names.
     * @param page pagination info
     * @param status order status (optional)
     * @param keyword search keyword: order number or customer name (optional)
     * @return order VO list
     */
    IPage<vtc.xueqing.flower.vo.OrderVO> getAllOrders(Page<vtc.xueqing.flower.vo.OrderVO> page, String status, String keyword);
    
    /**
     * Get care knowledge list (admin).
     * @param page pagination info
     * @param keyword search keyword: title (optional)
     * @param category category (optional)
     * @param status status (optional)
     * @return care knowledge list
     */
    IPage<vtc.xueqing.flower.entity.CareKnowledge> getKnowledgeList(Page<vtc.xueqing.flower.entity.CareKnowledge> page, String keyword, String category, String status);
    
    /**
     * Get care knowledge detail (admin).
     * @param id knowledge ID
     * @return care knowledge detail
     */
    vtc.xueqing.flower.entity.CareKnowledge getKnowledgeById(Long id);
    
    /**
     * Create care knowledge (admin).
     * @param knowledge care knowledge info
     * @return created care knowledge
     */
    vtc.xueqing.flower.entity.CareKnowledge createKnowledge(vtc.xueqing.flower.entity.CareKnowledge knowledge);
    
    /**
     * Update care knowledge (admin).
     * @param knowledge care knowledge info
     * @return updated care knowledge
     */
    vtc.xueqing.flower.entity.CareKnowledge updateKnowledge(vtc.xueqing.flower.entity.CareKnowledge knowledge);
    
    /**
     * Update care knowledge status (admin).
     * @param id knowledge ID
     * @param status status
     * @return updated care knowledge
     */
    vtc.xueqing.flower.entity.CareKnowledge updateKnowledgeStatus(Long id, String status);
    
    /**
     * Delete care knowledge (admin).
     * @param id knowledge ID
     */
    void deleteKnowledge(Long id);
    
    /**
     * Get admin profile by ID.
     * @param adminId admin ID
     * @return admin info
     */
    Administrator getProfileById(Long adminId);
    
    /**
     * Update admin profile.
     * @param adminId admin ID
     * @param name name
     * @return updated admin info
     */
    Administrator updateProfile(Long adminId, String name);
    
    /**
     * Change admin password.
     * @param adminId admin ID
     * @param oldPassword old password
     * @param newPassword new password
     */
    void updatePassword(Long adminId, String oldPassword, String newPassword);
}
