package com.github.stazxr.zblog.core.base;

import com.github.stazxr.zblog.core.util.ToStringUtils;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础请求实体
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
@ApiModel("基础DTO")
public class BaseDto implements Serializable {
    private static final long serialVersionUID = -7385869795196886066L;

    @Override
    public String toString() {
        return ToStringUtils.buildString(this);
    }
}
