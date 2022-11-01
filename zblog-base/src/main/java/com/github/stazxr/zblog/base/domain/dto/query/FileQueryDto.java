package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件列表查询参数
 *
 * @author SunTao
 * @since 2022-10-17
 */
@Getter
@Setter
@ToString
public class FileQueryDto extends PageParam {
    /**
     * 文件名
     */
    private String filename;

    /**
     * 上传方式
     */
    private Integer storageType;

    /**
     * 创建开始时间
     */
    private String createStartTime;

    /**
     * 创建结束时间
     */
    private String createEndTime;
}
