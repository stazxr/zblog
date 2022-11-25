package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 栏目查询
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
public class ArticleColumnQueryDto extends PageParam {
    /**
     * 栏目名称
     */
    private String name;

    /**
     * 栏目状态
     */
    private Boolean enabled;
}
