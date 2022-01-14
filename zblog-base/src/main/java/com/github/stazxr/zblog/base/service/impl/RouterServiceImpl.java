package com.github.stazxr.zblog.base.service.impl;

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
}
