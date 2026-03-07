package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Supplier entity class
 * Corresponds to database table: suppliers
 */
@Data
@TableName("suppliers")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Supplier ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Supplier name
     */
    private String name;

    /**
     * Contact person
     */
    private String contactPerson;

    /**
     * Phone number
     */
    private String phone;

    /**
     * Email
     */
    private String email;

    /**
     * Address
     */
    private String address;

    /**
     * Description
     */
    private String description;

    /**
     * Rating: 1.0-5.0
     */
    private BigDecimal rating;

    /**
     * Status: ACTIVE, INACTIVE, SUSPENDED
     */
    private String status;

    /**
     * Created at
     */
    private LocalDateTime createDate;

    /**
     * Updated at
     */
    private LocalDateTime updateDate;
}
