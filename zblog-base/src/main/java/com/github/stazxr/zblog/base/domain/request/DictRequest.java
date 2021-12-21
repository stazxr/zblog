package com.github.stazxr.zblog.base.domain.request;

import com.github.stazxr.zblog.core.base.BaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典相关的查询条件
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
public class DictRequest extends BaseRequest {
    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典 KEY
     */
    private String key;
}
