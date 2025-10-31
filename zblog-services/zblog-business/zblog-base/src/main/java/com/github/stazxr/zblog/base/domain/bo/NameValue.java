package com.github.stazxr.zblog.base.domain.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 基于 {name: '', value: ''} 的数据格式
 *
 * @author SunTao
 * @since 2023-05-30
 */
@Getter
@Setter
public class NameValue {
    private String name;

    private Object value;

    @Override
    public String toString() {
        return "{'name': '" + name + "', value: '" + value.toString() + "'}";
    }
}
