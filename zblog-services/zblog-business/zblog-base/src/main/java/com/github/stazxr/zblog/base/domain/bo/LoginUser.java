package com.github.stazxr.zblog.base.domain.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 左侧菜单元数据
 *
 * @author SunTao
 * @since 2023-06-29
 */
@Getter
@Setter
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 7908166496446814773L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户令牌
     */
    private String accessToken;
}
