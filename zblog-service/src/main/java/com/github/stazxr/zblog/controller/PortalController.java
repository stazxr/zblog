package com.github.stazxr.zblog.controller;

import com.github.stazxr.zblog.service.WebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台管理
 *
 * @author SunTao
 * @since 2022-11-25
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portal")
public class WebController {
    private final WebService webService;


}
