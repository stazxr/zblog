package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 逻辑文件
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Getter
@Setter
@TableName("file")
public class File implements Serializable {
    private static final long serialVersionUID = 8310644673548597060L;

    /**
     * 文件id
     */
    @TableId
    private Long id;

    /**
     * 物理文件id
     */
    private Long storageId;

    /**
     * 原文件名称
     */
    private String originalFilename;

    /**
     * 创建用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}
