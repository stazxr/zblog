package com.github.stazxr.zblog.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文章临时内容
 *
 * @author SunTao
 * @since 2023-01-13
 */
@Getter
@Setter
@ToString
public class ArticleTmpContentVo {
    /**
     * 记录序列
     */
    private Long id;

    /**
     * 文章序列
     */
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String remark;

    /**
     * 文章字数
     */
    private String count;

    /**
     * 保存时间
     */
    private String saveTime;
}
