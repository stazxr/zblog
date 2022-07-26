package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Router;

import java.util.Set;

/**
 * 路由业务层
 *
 * @author SunTao
 * @since 2021-07-10
 */
public interface RouterService extends IService<Router> {
    /**
     * 移除所有的路由信息
     */
    void clearRouter();

    /**
     * 计算接口的访问级别
     *
     * @param requestUri    请求地址
     * @param requestMethod 请求方式
     * @return 访问级别
     */
    int calculateInterfaceLevel(String requestUri, String requestMethod);

    /**
     * 根据请求地址查询允许访问的角色列表
     *
     * @param requestUri    请求地址
     * @param requestMethod 请求方式
     * @return allowed roles
     */
    Set<String> findRoles(String requestUri, String requestMethod);
}
