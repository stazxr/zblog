package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查
 *
 * @author SunTao
 * @since 2022-10-19
 */
@RestController
public class HealthCheckController {
    /**
     * 健康检查
     *
     * @return Result.success
     */
    @GetMapping("/healthCheck")
    @Router(name = "健康检查", code = "healthCheck", level = BaseConst.PermLevel.OPEN)
    public Result healthCheck() {
        return Result.success();
    }
}