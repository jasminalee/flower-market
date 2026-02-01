package vtc.xueqing.flower.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product review VO for admin review listings.
 * Includes associated user and product information.
 */
@Data
public class ProductReviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
        * Review ID.
     */
    private Long id;

    /**
        * User ID.
     */
    private Long userId;

    /**
        * Username.
     */
    private String userName;

    /**
        * Product ID.
     */
    private Long prodId;

    /**
        * Product name.
     */
    private String productName;

    /**
        * Product image.
     */
    private String productImage;

    /**
        * Order ID.
     */
    private Long orderId;

    /**
        * Rating: 1-5 stars.
     */
    private Integer rating;

    /**
        * Review content.
     */
    private String content;

    /**
        * Review images (JSON array).
     */
    private String images;

    /**
        * Verified purchase flag: 0-unverified, 1-verified.
     */
    private Integer verified;

    /**
        * Status: PENDING-pending review, APPROVED-approved, REJECTED-rejected.
     */
    private String status;

    /**
     * Created time.
     */
    private LocalDateTime createDate;
}
