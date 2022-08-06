package com.github.stazxr.zblog.core.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * PageParam
 *
 * @author SunTao
 * @since 2021-12-21
 */
@Getter
@Setter
public class PageParam implements Serializable {
    private Integer page;

    private Integer pageSize;
}
