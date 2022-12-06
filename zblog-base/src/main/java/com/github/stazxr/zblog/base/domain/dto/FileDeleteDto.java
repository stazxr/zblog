package com.github.stazxr.zblog.base.domain.dto;

import lombok.Data;

/**
 * 文件删除参数信息
 *
 * @author SunTao
 * @since 2022-12-02
 */
@Data
public class FileDeleteDto {
    private Long fileId;

    private Long businessId;
}
