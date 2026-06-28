package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemeDto;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemePageDto;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemeStatusDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemePageQueryDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemeQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Theme;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;

import java.util.List;

/**
 * 主题管理业务层
 *
 * @author SunTao
 * @since 2026-06-14
 */
public interface ThemeService extends IService<Theme> {
    /**
     * 分页查询主题列表
     *
     * @param queryDto 查询参数
     * @return IPage<ThemeVo>
     */
    IPage<ThemeVo> queryThemeListByPage(ThemeQueryDto queryDto);

    /**
     * 查询主题详情
     *
     * @param themeId 主题id
     * @return ThemeVo
     */
    ThemeVo queryThemeDetail(Long themeId);

    /**
     * 新增主题
     *
     * @param themeDto 主题信息
     */
    void addTheme(ThemeDto themeDto);

    /**
     * 编辑主题
     *
     * @param themeDto 主题信息
     */
    void editTheme(ThemeDto themeDto);

    /**
     * 设置用户主题状态
     *
     * @param themeStatusDto 主题状态信息
     */
    void editUserThemeStatus(ThemeStatusDto themeStatusDto);

    /**
     * 设置系统主题状态
     *
     * @param themeStatusDto 主题状态信息
     */
    void editSystemThemeStatus(ThemeStatusDto themeStatusDto);

    /**
     * 升级用户主题为系统主题
     *
     * @param themeId 主题id
     */
    void upgradeTheme(Long themeId);

    /**
     * 删除主题
     *
     * @param themeId 主题id
     */
    void deleteTheme(Long themeId);

    /**
     * 查询主题页面配置列表
     *
     * @param queryDto 查询参数
     * @return List<ThemePageVo>
     */
    List<ThemePageVo> queryThemePageList(ThemePageQueryDto queryDto);

    /**
     * 查询主题页面详情
     *
     * @param themePageId 主题页面id
     * @return ThemePageVo
     */
    ThemePageVo queryThemePageDetail(Long themePageId);

    /**
     * 新增主题页面
     *
     * @param themePageDto 主题页面信息
     */
    void addThemePage(ThemePageDto themePageDto);

    /**
     * 编辑主题页面
     *
     * @param themePageDto 主题页面信息
     */
    void editThemePage(ThemePageDto themePageDto);

    /**
     * 删除主题页面
     *
     * @param themePageId 主题页面id
     */
    void deleteThemePage(Long themePageId);
}
