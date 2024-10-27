package com.github.stazxr.zblog.domain.bo;

import com.github.stazxr.zblog.core.base.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 门户-文章页面简单信息
 *
 * @author SunTao
 * @since 2023-01-06
 */
@Getter
@Setter
@ToString
public class ArticleSimpleData extends BaseVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;
}
