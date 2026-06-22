package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemeQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Theme;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 主题管理数据层
 *
 * @author SunTao
 * @since 2026-06-14
 */
public interface ThemeMapper extends BaseMapper<Theme> {
    /**
     * 分页查询主题列表
     *
     * @param queryDto 查询参数
     * @return IPage<ThemeVo>
     */
    IPage<ThemeVo> selectThemeList(@Param("page") Page<ThemeVo> page, @Param("query") ThemeQueryDto queryDto);

    /**
     * 查询主题详情
     *
     * @param themeId 主题id
     * @return ThemeVo
     */
    ThemeVo selectThemeDetail(@Param("themeId") Long themeId);

    /**
     * 更新主题信息
     *
     * @param theme 主题
     * @return boolean
     */
    boolean updateThemeBaseInfo(@Param("t") Theme theme);
}
