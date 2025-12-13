package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 养护知识实体类
 * 对应数据库表：care_knowledge
 */
@Data
@TableName("care_knowledge")
public class CareKnowledge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 知识ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 关键词
     */
    @TableField("keywords")
    private String keywords;

    /**
     * 封面图片
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 分类
     */
    @TableField("category")
    private String category;

    /**
     * 作者
     */
    @TableField("author")
    private String author;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 状态：DRAFT-草稿，PUBLISHED-已发布
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    @TableField("update_date")
    private LocalDateTime updateDate;
}
