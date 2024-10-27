package com.github.stazxr.zblog.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 标签云
 *
 * @author SunTao
 * @since 2023-02-13
 */
@Getter
@Setter
public class CloudTagVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签对应的文章数
     */
    private String value;
}
