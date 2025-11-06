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

    public Object getValue() {
        if (Boolean.TRUE.toString().equals(value) || Boolean.FALSE.toString().equals(value)) {
            // boolean 适配
            return Boolean.valueOf((String) value);
        }
        try {
            // int 适配
            return Integer.valueOf((String) value);
        } catch (Exception ignored) { }
        try {
            // long 适配
            return Long.valueOf((String) value);
        } catch (Exception ignored) { }
        return value;
    }

    @Override
    public String toString() {
        return "{'name': '" + name + "', value: '" + value.toString() + "'}";
    }
}
