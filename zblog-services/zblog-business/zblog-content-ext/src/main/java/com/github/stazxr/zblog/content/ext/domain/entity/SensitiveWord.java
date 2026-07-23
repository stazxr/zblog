package com.github.stazxr.zblog.content.ext.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 敏感词
 *
 * @author SunTao
 * @since 2026-07-23
 */
@Getter
@Setter
public class SensitiveWord implements Serializable {
    private static final long serialVersionUID = 372111043576089706L;

    /**
     * 敏感词id
     */
    @TableId
    private Long id;

    /**
     * 敏感词
     */
    private String word;

    /**
     * 类型
     */
    private String type;

    /**
     * 风险等级
     */
    private Integer level;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改用户
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
