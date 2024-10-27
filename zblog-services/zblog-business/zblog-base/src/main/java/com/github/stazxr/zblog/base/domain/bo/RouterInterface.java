package com.github.stazxr.zblog.base.domain.bo;

import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.entity.Router;
import lombok.Data;

import java.util.List;

/**
 * 路由 - 接口信息封装
 *
 * @author SunTao
 * @since 2022-06-23
 */
@Data
public class RouterInterface {
    /**
     * 路由列表
     */
    List<Router> routers;

    /**
     * 受权限管控的接口列表
     */
    List<Interface> permInterfaces;

    /**
     * 不可以访问的接口列表
     */
    List<Interface> nullInterfaces;
}
