package com.github.stazxr.zblog.content.ext.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemeDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemeQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Theme;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;

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
     * 删除主题
     *
     * @param themeId 主题id
     */
    void deleteTheme(Long themeId);
}
