package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.entity.Router;

import java.util.Map;

/**
 * 路由业务层
 *
 * @author SunTao
 * @since 2021-07-10
 */
public interface RouterService extends IService<Router> {
    /**
     * 清除路由信息
     */
    void clearRouter();

    /**
     * 获取路由白名单
     *
     * @return whiteList
     */
    Map<String, String> getRouterWhiteList();

    /**
     * 获取路由黑名单
     *
     * @return blackList
     */
    Map<String, String> getRouterBlackList();

    /**
     * 根据请求URL和Method查询路由信息
     *
     * @param requestUrl 请求URL
     * @param requestMethod 请求Method
     * @return Router
     */
    Router selectByUrlAndMethod(String requestUrl, String requestMethod);
}
