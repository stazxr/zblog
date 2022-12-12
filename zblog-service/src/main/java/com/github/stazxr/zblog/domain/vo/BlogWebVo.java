package com.github.stazxr.zblog.domain.vo;

import com.github.stazxr.zblog.domain.dto.setting.OtherInfo;
import com.github.stazxr.zblog.domain.dto.setting.SocialInfo;
import com.github.stazxr.zblog.domain.dto.setting.WebInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 博客前台信息
 *
 * @author SunTao
 * @since 2022-12-09
 */
@Getter
@Setter
@ToString
public class BlogWebVo {
    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 分类数量
     */
    private Integer categoryCount;

    /**
     * 标签数量
     */
    private Integer tagCount;

    /**
     * 访问量
     */
    private Integer viewsCount;

    /**
     * 网站信息
     */
    private WebInfo webInfo;

    /**
     * 社交信息
     */
    private SocialInfo socialInfo;

    /**
     * 其他信息
     */
    private OtherInfo otherInfo;
}
