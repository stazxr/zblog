package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.rest.IgnoreResult;
import com.github.stazxr.zblog.bas.router.ApiVersion;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.IgnoreLog;
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
@Api(value = "HealthCheckController", tags = { "健康检查" })
public class HealthCheckController {
    /**
     * 健康检查
     *
     * @return true
     */
    @IgnoreLog
    @IgnoreResult
    @GetMapping("/api/healthCheck")
    @ApiOperation(value = "健康检查", notes = "服务心跳，不做业务逻辑")
    @ApiVersion(BaseConst.ApiVersion.V_4_0_0)
    @Router(name = "健康检查", code = "HEALQ001", level = RouterLevel.OPEN)
    public boolean healthCheck() {
        return true;
    }
}
