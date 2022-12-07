package com.github.stazxr.zblog.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 标签查询
 *
 * @author SunTao
 * @since 2022-11-24
 */
@Getter
@Setter
@ToString
public class ArticleTagQueryDto extends PageParam {
    /**
     * 标签名称
     */
    private String name;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 标签类型
     */
    private Integer type;

    /**
     * 标签状态
     */
    private Boolean enabled;
}
