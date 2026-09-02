package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 评论表情
 *
 * @author suntao
 * @since 2026-08-31
 */
@Getter
@Setter
@TableName("comment_emoji")
public class CommentEmoji implements Serializable {
    private static final long serialVersionUID = 2162719139518452082L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 表情名称
     */
    private String name;

    /**
     * 表情编码 [名称]
     */
    private String code;

    /**
     * 表情图片路径
     */
    private String url;

    /**
     * 排序
     */
    private Integer sort;
}