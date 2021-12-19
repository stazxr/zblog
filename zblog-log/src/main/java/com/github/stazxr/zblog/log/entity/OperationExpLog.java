package com.github.stazxr.zblog.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 操作异常日志
 *
 * @author SunTao
 * @since 2021-05-16
 */
@TableName("sys_operate_exp_log_tbl")
@Getter
@Setter
@ToString
public class OperationExpLog extends BaseEntity {
    /**
     * serialId
     */
    private static final long serialVersionUID = -2995022055544531607L;

    /**
     * 操作ID
     */
    private String operateId;

    /**
     * 操作方法
     */
    private String operateMethod;

    /**
     * 操作参数
     */
    private String operateParam;

    /**
     * 异常名称
     */
    private String expName;

    /**
     * 异常信息
     */
    private String expMessage;

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
     * 发生异常的时间
     */
    private String expTime;
}
