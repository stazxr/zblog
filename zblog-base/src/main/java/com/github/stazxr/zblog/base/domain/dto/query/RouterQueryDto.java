package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 路由查询
 *
 * @author SunTao
 * @since 2022-09-16
 */
@Getter
@Setter
@ToString
public class RouterQueryDto extends PageParam {
    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口编码
     */
    private String code;

    /**
     * 接口地址
     */
    private String uri;

    /**
     * 访问级别
     */
    private String level;

    /**
     * 接口状态
     */
    private String status;

    /**
     * 日志状态
     */
    private Boolean logShowed;

    /**
     * 字典KEY
     */
    private String dictKey;
}
