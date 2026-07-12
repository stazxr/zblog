package com.github.stazxr.zblog.base.domain.bo;

import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.MenuVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 登录用户信息
 *
 * @author SunTao
 * @since 2026-05-23
 */
@Getter
@Setter
@ApiModel("登录用户信息")
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 7908166496446814773L;

    /**
     * 用户基础信息
     */
    @ApiModelProperty("用户基础信息")
    private User user;

    /**
     * 用户权限列表
     */
    @ApiModelProperty("用户权限列表")
    private List<String> perms;

    /**
     * 用户菜单列表
     */
    @ApiModelProperty("用户菜单列表")
    private List<MenuVo> menus;
}
