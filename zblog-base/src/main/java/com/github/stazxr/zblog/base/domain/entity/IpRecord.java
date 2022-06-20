package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.stazxr.zblog.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * IP记录（留存各种操作的地址）
 *
 * @author SunTao
 * @since 2022-05-31
 */
@Getter
@Setter
@TableName("ip_record")
public class IpRecord extends BaseEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 业务ID
     */
    private Long pid;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 地址
     */
    private String address;
}
