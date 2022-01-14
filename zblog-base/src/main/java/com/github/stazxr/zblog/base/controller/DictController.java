package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.DictService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理
 *
 * @author SunTao
 * @since 2021-02-20
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典管理")
@RequestMapping("/api/dict")
public class DictController {
    private final DictService dictService;
}
