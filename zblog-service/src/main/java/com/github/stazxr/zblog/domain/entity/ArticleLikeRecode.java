package com.github.stazxr.zblog.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 文章点赞记录
 *
 * @author SunTao
 * @since 2020-03-08
 */
@Getter
@Setter
@TableName("article_like_recode")
public class ArticleLikeRecode {
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
     * 来源，1：系统内用户；2：匿名
     */
    private Integer source;

    /**
     * 点赞用户访问IP
     */
    private String accessIp;

    /**
     * 点赞用户访问地址
     */
    private String accessAddress;

    /**
     * 点赞用户：匿名/系统用户名
     */
    private String accessUser;

    /**
     * 点赞时间
     */
    private String accessTime;
}
