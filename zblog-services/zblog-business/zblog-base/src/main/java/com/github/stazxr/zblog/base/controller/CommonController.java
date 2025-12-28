package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共管理
 *
 * @author SunTao
 * @since 2025-12-27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
@Api(value = "CommonController", tags = { "公共管理" })
public class CommonController {
    @Value("${zblog.security.PublicKey}")
    private String systemPublicKey;

    /**
     * 获取系统公钥
     *
     * @return 系统公钥
     */
    @GetMapping("/querySystemPublicKey")
    @ApiOperation(value = "获取系统公钥")
    @ApiVersion(group = { BaseConst.ApiVersion.V_5_0_0 })
    @Router(name = "获取系统公钥", code = "COMMQ001", level = RouterLevel.PUBLIC)
    public String querySystemPublicKey() {
        return systemPublicKey;
    }
}
