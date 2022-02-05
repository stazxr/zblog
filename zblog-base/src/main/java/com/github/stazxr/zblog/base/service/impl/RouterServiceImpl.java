package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.mapper.RouterMapper;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.core.base.BaseConst;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 路由业务实现层
 *
 * @author SunTao
 * @since 2021-07-10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RouterServiceImpl extends ServiceImpl<RouterMapper, Router> implements RouterService {
    @Resource
    private RouterMapper routerMapper;

    @Resource
    private DictService dictService;

    /**
     * 移除所有的路由信息
     */
    @Override
    public void clearRouter() {
        routerMapper.clearRouter();
    }

    /**
     * 获取路由白名单
     *
     * @return whiteList
     */
    @Override
    public Map<String, String> getRouterWhiteList() {
        return dictService.selectItems(BaseConst.DictKey.ROUTER_WHITE_LIST);
    }

    /**
     * 获取路由黑名单
     *
     * @return blackList
     */
    @Override
    public Map<String, String> getRouterBlackList() {
        return dictService.selectItems(BaseConst.DictKey.ROUTER_BLACK_LIST);
    }

    /**
     * 根据请求URL和Method查询路由信息
     *
     * @param url    请求URL
     * @param method 请求Method
     * @return Router
     */
    @Override
    public Router selectByUrlAndMethod(String url, String method) {
        return routerMapper.selectOne(queryBuild().eq(Router::getUrl, url).eq(Router::getMethod, method));
    }

    private LambdaQueryWrapper<Router> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
