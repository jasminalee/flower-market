package vtc.xueqing.flower.vo;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车视图对象
 * 包含商品详细信息
 */
@Data
public class ShoppingCartVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 是否选中：0-未选中，1-已选中
     */
    private Integer selected;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 商品描述
     */
    private String prodDesc;

    /**
     * 商品主图
     */
    private String prodImage;

    /**
     * 商品价格
     */
    private BigDecimal prodPrice;

    /**
     * 商品库存
     */
    private Integer prodStock;

    /**
     * 商家ID
     */
    private Long merchId;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 添加时间
     */
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;
}
