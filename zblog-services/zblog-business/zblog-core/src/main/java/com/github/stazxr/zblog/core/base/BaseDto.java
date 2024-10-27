package com.github.stazxr.zblog.core.base;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * BaseDto
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Slf4j
@Getter
@Setter
public class BaseDto implements Serializable {
    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            log.error("dto toString builder catch an error", e);
            return "toString builder catch an error";
        }
        return builder.toString();
    }
}
