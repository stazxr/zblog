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
}
