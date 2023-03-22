package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.annotation.IgnoreResult;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据测试
 *
 * @author SunTao
 * @since 2023-02-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Api(value = "TestController", tags = { "接口测试控制器" })
public class TestController {
    /**
     * 测试IP工具类
     *
     * @param ip IP地址
     * @return IP来源
     */
    @IgnoredLog
    @IgnoreResult
    @GetMapping(value = "/getIpSource")
    @ApiOperation(value = "测试IP工具类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ip", value = "ip地址", required = true, dataTypeClass = String.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "测试IP工具类", code = "getIpSourceByTest", level = BaseConst.PermLevel.OPEN)
    public String testIpUtils(@RequestParam String ip) {
        log.info("IpImplUtils.getIpSourceByHttp({}): {}", ip, IpImplUtils.getIpSourceByHttp(ip));
        log.info("IpImplUtils.getIpSourceByLocal({}): {}", ip, IpImplUtils.getIpSourceByLocal(ip));
        return IpImplUtils.getIpSource(ip);
    }
}
