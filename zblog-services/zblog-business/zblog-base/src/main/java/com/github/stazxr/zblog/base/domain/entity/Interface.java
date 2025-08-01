package com.github.stazxr.zblog.base.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口
 *
 * @author SunTao
 * @since 2022-06-23
 */
@Getter
@Setter
@TableName("interface")
public class Interface implements Serializable {
    private static final long serialVersionUID = 3483968961273114029L;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 接口请求地址
     */
    private String uri;

    /**
     * 接口请求方式
     */
    private String method;
}
