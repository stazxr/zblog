package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 版本查询
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Getter
@Setter
@ToString
public class VersionQueryDto extends PageParam {
    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 版本描述
     */
    private String updateContent;
}
