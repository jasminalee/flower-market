package vtc.xueqing.flower.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Care Knowledge Comment Entity
 */
@Data
@TableName("knowledge_comments")
public class KnowledgeComment implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("knowledge_id")
    private Long knowledgeId;

    @TableField("user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("content")
    private String content;

    @TableField("status")
    private String status;

    @TableField("create_date")
    private LocalDateTime createDate;
}
