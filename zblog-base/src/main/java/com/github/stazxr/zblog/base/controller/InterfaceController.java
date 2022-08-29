package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.domain.dto.InterfaceQueryDto;
import com.github.stazxr.zblog.base.service.InterfaceService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.model.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口管理
 *
 * @author SunTao
 * @since 2022-08-29
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interfaces")
public class InterfaceController {
    private final InterfaceService interfaceService;

    /**
     * 查询权限对应的接口列表
     *
     * @param queryDto 查询参数
     * @return interfaceList
     */
    @GetMapping(value = "/queryPermInterface")
    @Router(name = "查询权限对应的接口列表", code = "queryPermInterface", level = BaseConst.PermLevel.PUBLIC)
    public Result queryPermInterface(InterfaceQueryDto queryDto) {
        return Result.success().data(interfaceService.queryPermInterface(queryDto));
    }
}
