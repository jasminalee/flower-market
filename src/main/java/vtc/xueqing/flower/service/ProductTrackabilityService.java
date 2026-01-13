package vtc.xueqing.flower.service;

import vtc.xueqing.flower.entity.ProductTrackability;

/**
 * Product traceability service interface.
 */
public interface ProductTrackabilityService {
    
    /**
     * Get traceability info by product ID.
     * @param prodId product ID
     * @return traceability info
     */
    ProductTrackability getByProductId(Long prodId);

    /**
     * Create or update product traceability info (merchant side).
     * @param productTrackability traceability info
     * @return traceability info
     */
    ProductTrackability saveOrUpdate(ProductTrackability productTrackability);

    /**
     * Delete traceability info by product ID.
     * @param prodId product ID
     */
    void deleteByProductId(Long prodId);
}
