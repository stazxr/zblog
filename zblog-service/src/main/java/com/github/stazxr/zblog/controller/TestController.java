package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.util.IpImplUtils;
import com.github.stazxr.zblog.log.annotation.IgnoredLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class TestController {
    /**
     * 测试IP工具类
     *
     * @param ip IP地址
     * @return IP来源
     */
    @IgnoredLog
    @GetMapping(value = "/getIpSource")
    @Router(name = "测试IP工具类", code = "getIpSourceByTest", level = BaseConst.PermLevel.OPEN)
    public String testIpUtils(String ip) {
        log.info("IpImplUtils.getIpSourceByHttp({}): {}", ip, IpImplUtils.getIpSourceByHttp(ip));
        log.info("IpImplUtils.getIpSourceByLocal({}): {}", ip, IpImplUtils.getIpSourceByLocal(ip));
        return IpImplUtils.getIpSource(ip);
    }
}
