package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典查询
 *
 * @author SunTao
 * @since 2022-09-20
 */
@Getter
@Setter
@ToString
public class DictQueryDto extends PageParam {
    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典Key
     */
    private String dictKey;

    /**
     * 字典状态
     */
    private Boolean enabled;
}
