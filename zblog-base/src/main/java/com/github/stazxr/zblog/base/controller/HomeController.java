package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.service.HomeService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.model.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.github.stazxr.zblog.core.base.BaseConst.PermLevel.*;

/**
 * 面板管理
 *
 * @author SunTao
 * @since 2022-07-19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {
    private final HomeService homeService;

    /**
     * 获取首页面板的统计数据
     *
     * @return HomePanelDataCountVo
     */
    @GetMapping("/getHomePanelDataCount")
    @Router(name = "获取首页面板的统计数据", code = "getHomePanelDataCount", level = PUBLIC)
    public Result getHomePanelDataCount() {
        return Result.success().data(homeService.getHomePanelDataCount());
    }

    /**
     * 获取首页面板的统计数据
     *
     * @return SingleLineChartDataVo
     */
    @GetMapping("/getHomePanelDetailDataByType")
    @Router(name = "获取首页面板的折现图标数据", code = "getHomePanelDetailDataByType", level = PUBLIC)
    public Result getHomePanelDetailDataByType(@RequestParam String type) {
        return Result.success().data(homeService.getHomePanelDetailDataByType(type));
    }
}
