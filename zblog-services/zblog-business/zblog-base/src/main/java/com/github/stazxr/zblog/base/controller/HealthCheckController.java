package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.msg.Result;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author SunTao
 * @since 2022-10-19
 */
@RestController
@Api(value = "HealthCheckController", tags = { "健康检查控制器" })
public class HealthCheckController {
    /**
     * 健康检查
     *
     * @return Result.success
     */
    @GetMapping("/healthCheck")
    @ApiOperation(value = "健康检查", notes = "服务心跳，不做业务逻辑")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "健康检查", code = "healthCheck", level = RouterLevel.OPEN)
    public Result healthCheck() {
        return Result.success("服务心跳正常");
    }
}