package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章浏览记录
 *
 * @author SunTao
 * @since 2020-03-08
 */
@Getter
@Setter
@TableName("article_show_record")
public class ArticleShowRecode {
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
