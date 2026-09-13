package com.github.stazxr.zblog.content.ext.domain.dto.query;

import com.github.stazxr.zblog.core.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 评论查询参数
 *
 * @author SunTao
 * @since 2026-09-09
 */
@Getter
@Setter
@ApiModel("评论查询参数")
public class CommentQueryDto extends PageParam {
    private static final long serialVersionUID = 5151131693274284637L;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", notes = "模糊搜索")
    private String content;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称", notes = "模糊搜索")
    private String nickname;

    /**
     * 评论归属IP
     */
    @ApiModelProperty(value = "评论归属IP", notes = "精确搜索")
    private String ip;

    /**
     * 评论类型
     */
    @ApiModelProperty(value = "评论类型")
    private Integer type;

    /**
     * 评论级别
     */
    @ApiModelProperty(value = "评论级别")
    private Integer level;

    /**
     * 评论状态
     */
    @ApiModelProperty(value = "评论状态")
    private Integer status;

    /**
     * 评论开始时间
     */
    @ApiModelProperty(value = "评论开始时间", example = "2023-02-16 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createStartTime;

    /**
     * 评论结束时间
     */
    @ApiModelProperty(value = "评论结束时间", example = "2023-02-16 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createEndTime;
}
