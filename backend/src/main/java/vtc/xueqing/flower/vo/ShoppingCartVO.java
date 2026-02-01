package vtc.xueqing.flower.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Shopping cart view object with product details.
 * Contains product details
 */
@Data
public class ShoppingCartVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * Shopping cart ID.
     */
    private Long id;

    /**
    * User ID.
     */
    private Long userId;

    /**
    * Product ID.
     */
    private Long prodId;

    /**
    * Quantity of the product.
     */
    private Integer quantity;

    /**
    * Selected flag: 0-unselected, 1-selected.
     */
    private Integer selected;

    /**
    * Product name.
     */
    private String prodName;

    /**
    * Product description.
     */
    private String prodDesc;

    /**
    * Product main image.
     */
    private String prodImage;

    /**
    * Product price.
     */
    private BigDecimal prodPrice;

    /**
    * Product stock.
     */
    private Integer prodStock;

    /**
    * Merchant ID.
     */
    private Long merchId;

    /**
    * Merchant name.
     */
    private String merchantName;

    /**
    * Created time.
     */
    private LocalDateTime createDate;

    /**
    * Updated time.
     */
    private LocalDateTime updateDate;
}
