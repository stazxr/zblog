package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.InterfaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
