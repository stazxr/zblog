package com.github.stazxr.zblog.base.domain.bo;

import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.entity.Router;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 路由和接口信息封装
 *
 * @author SunTao
 * @since 2022-06-23
 */
@Getter
@Setter
public class RouterInterface {
    /**
     * 路由列表
     */
    List<Router> routers;

    /**
     * 接口列表
     */
    List<Interface> interfaces;

    public RouterInterface(List<Router> routers, List<Interface> interfaces) {
        this.routers = routers;
        this.interfaces = interfaces;
    }
}
