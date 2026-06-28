package com.github.stazxr.zblog.content.ext.mapper;

import com.github.stazxr.zblog.content.ext.domain.entity.ThemePage;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 主题封面管理数据层
 *
 * @author SunTao
 * @since 2026-06-14
 */
public interface ThemePageMapper extends BaseMapper<ThemePage> {
    /**
     * 根据主题id删除页面配置
     *
     * @param themeId 主题id
     */
    void deleteByThemeId(@Param("themeId") Long themeId);

    /**
     * 查询主题页面配置列表
     *
     * @param themeId 主题id
     * @return List<ThemePageVo>
     */
    List<ThemePageVo> selectThemePage(@Param("themeId") Long themeId);

    /**
     * 查询主题页面详情
     *
     * @param themePageId 主题页面id
     * @return ThemePageVo
     */
    ThemePageVo selectThemePageDetail(@Param("themePageId") Long themePageId);
}
