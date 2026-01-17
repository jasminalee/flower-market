package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Care knowledge entity class
 * Corresponds to database table: care_knowledge
 */
@Data
@TableName("care_knowledge")
public class CareKnowledge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Care knowledge ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Title
     */
    @TableField("title")
    private String title;

    /**
     * Content
     */
    @TableField("content")
    private String content;

    /**
     * Keywords
     */
    @TableField("keywords")
    private String keywords;

    /**
     * Cover image
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * Category
     */
    @TableField("category")
    private String category;

    /**
     * Author
     */
    @TableField("author")
    private String author;

    /**
     * View count
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * Status: DRAFT-draft, PUBLISHED-published
     */
    @TableField("status")
    private String status;

    /**
     * Creation time
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * Update time
     */
    @TableField("update_date")
    private LocalDateTime updateDate;
}
