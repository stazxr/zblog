package com.github.stazxr.zblog.core.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * BaseRequest
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
public class BaseRequest implements Serializable {
    private Integer pageNum;

    private Integer pageSize;
}
