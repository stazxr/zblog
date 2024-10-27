package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章浏览记录
 *
 * @author SunTao
 * @since 2023-02-08
 */
@Getter
@Setter
@TableName("article_view")
public class ArticleView extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户访问IP
     */
    private String accessIp;

    /**
     * 用户访问地址
     */
    private String accessAddress;

    /**
     * 访问时间
     */
    private String accessTime;
}
