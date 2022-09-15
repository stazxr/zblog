package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.vo.RouterVo;
import com.github.stazxr.zblog.base.service.RouterService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.util.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统路由管理
 *
 * @author SunTao
 * @since 2020-12-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/router")
public class RouterController {
    private final RouterService routerService;

    /**
     * 根据权限编码查询路由信息
     *
     * @param code 权限编码
     * @return routerVo
     */
    @GetMapping(value = "/queryRouterByCode")
    @Router(name = "根据权限编码查询路由信息", code = "queryRouterByCode", level = BaseConst.PermLevel.PUBLIC)
    public Result queryRouterByCode(String code) {
        RouterVo routerVo = routerService.queryRouterByCode(code);
        Assert.notNull(routerVo, "根据权限编码查询路由信息失败，数据不存在");
        return Result.success().data(routerVo);
    }
}
