package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.ProductTrackability;

/**
 * 产品溯源服务接口
 */
public interface ProductTrackabilityService {
    
    /**
     * 根据产品ID获取溯源信息
     * @param prodId 产品ID
     * @return 溯源信息
     */
    ProductTrackability getByProductId(Long prodId);
    
    /**
     * 创建或更新产品溯源信息（商家）
     * @param productTrackability 溯源信息
     * @return 溯源信息
     */
    ProductTrackability saveOrUpdate(ProductTrackability productTrackability);
    
    /**
     * 删除产品溯源信息
     * @param prodId 产品ID
     */
    void deleteByProductId(Long prodId);
}
