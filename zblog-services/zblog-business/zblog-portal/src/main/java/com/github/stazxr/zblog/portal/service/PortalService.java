package com.github.stazxr.zblog.portal.service;

import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.portal.domain.dto.BarrageMessageDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 门户管理业务层
 *
 * @author SunTao
 * @since 2027-07-07
 */
public interface PortalService {
    /**
     * 查询博客页面信息
     *
     * @return Map<String, List<ThemePageVo>>
     *     K: pageLabel
     *     V: List<ThemePageVo>
     */
    Map<String, List<ThemePageVo>> queryPageInfo();

    /**
     * 新增弹幕
     *
     * @param request 请求信息
     * @param barrageMessageDto 弹幕信息
     */
    void addBarrageMessage(HttpServletRequest request, BarrageMessageDto barrageMessageDto);
}
