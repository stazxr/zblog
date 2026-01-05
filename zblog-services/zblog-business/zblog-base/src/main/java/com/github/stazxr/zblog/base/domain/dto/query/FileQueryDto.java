package com.github.stazxr.zblog.base.domain.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 文件查询参数
 *
 * @author SunTao
 * @since 2022-10-17
 */
@Getter
@Setter
@ApiModel("文件查询参数")
public class FileQueryDto extends PageParam {
    private static final long serialVersionUID = 1052796568579684398L;

    /**
     * 上传方式
     */
    @ApiModelProperty(value = "上传方式，1：本地存储、2：阿里云OSS、3：七牛云OSS", example = "1")
    private Integer uploadType;

    /**
     * 文件名
     */
    @ApiModelProperty(value = "文件名", notes = "模糊查询")
    private String filename;

    /**
     * 文件MD5
     */
    @ApiModelProperty(value = "文件MD5", notes = "精确查询")
    private String fileMd5;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型", notes = "精确查询")
    private Integer businessType;

    /**
     * 业务状态
     */
    @ApiModelProperty(value = "业务状态")
    private Boolean hasBusiness;

    /**
     * 上传开始时间
     */
    @ApiModelProperty(value = "上传开始时间", example = "2023-02-16 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createStartTime;


    /**
     * 上传结束时间
     */
    @ApiModelProperty(value = "上传结束时间", example = "2023-02-16 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createEndTime;
}
