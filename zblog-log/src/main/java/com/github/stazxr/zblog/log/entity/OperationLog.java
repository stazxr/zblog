package com.github.stazxr.zblog.log.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 操作日志
 *
 * @author SunTao
 * @since 2021-05-16
 */
@TableName("sys_operate_log_tbl")
@Getter
@Setter
@ToString
public class OperationLog extends BaseEntity {
    /**
     * serialId
     */
    private static final long serialVersionUID = -5525097624549009616L;

    @TableId
    private String id;

    /**
     * 操作ID
     */
    private String operateId;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 操作模块
     */
    private String operateModule;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作级别
     */
    private String operateLevel;

    /**
     * 操作方法
     */
    private String operateMethod;

    /**
     * 操作参数
     */
    private String operateParam;

    /**
     * 操作结果（-1：无法确定；0：操作失败；1：操作成功）
     */
    private Integer operateResult;

    /**
     * 返回结果
     */
    private String returnResult;

    /**
     * 操作IP
     */
    private String operateIp;

    /**
     * 操作URL
     */
    private String operateUrl;

    /**
     * 操作用户
     */
    private String operateUser;

    /**
     * 操作时间
     */
    private String operateTime;
}
