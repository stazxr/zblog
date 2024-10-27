package com.github.stazxr.zblog.base.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("文件查询参数")
public class FileQueryDto extends PageParam {
    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名", notes = "模糊查询")
    private String filename;

    /**
     * 上传方式
     */
    @ApiModelProperty(value = "上传方式，0：默认、1：本地存储、2：阿里云OSS、3：七牛云OSS", example = "0")
    private Integer storageType;

    /**
     * 上传开始时间
     */
    @ApiModelProperty(value = "上传开始时间", example = "2023-02-16 00:00:00")
    private String createStartTime;

    /**
     * 上传结束时间
     */
    @ApiModelProperty(value = "上传结束时间", example = "2023-02-16 23:59:59")
    private String createEndTime;
}
