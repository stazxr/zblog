package com.github.stazxr.zblog.content.ext.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 敏感词管理
 *
 * @author SunTao
 * @since 2026-07-22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensitiveWords")
@Api(value = "SensitiveWordController", tags = "敏感词管理")
public class SensitiveWordController {

}
