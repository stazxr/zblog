package com.github.stazxr.zblog.content.ext.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemeQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Theme;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeType;
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

    /**
     * 设置用户主题状态为未激活
     *
     * @param themeType 主题类型
     * @param ownerId   所属用户
     */
    void inactiveUserTheme(@Param("themeType") ThemeType themeType, @Param("ownerId") Long ownerId);

    /**
     * 设置用户主题状态
     *
     * @param themeId 主题id
     * @param status  主题状态
     */
    void updateUserThemeStatus(@Param("themeId") Long themeId, @Param("status") Boolean status);

    /**
     * 设置系统主题状态为普通主题
     *
     * @param themeType 主题类型
     */
    void inactiveSystemTheme(@Param("themeType") ThemeType themeType);

    /**
     * 设置系统主题状态
     *
     * @param themeId 主题id
     * @param status  主题状态
     */
    void updateSystemThemeStatus(@Param("themeId") Long themeId, @Param("status") Boolean status);

    /**
     * 升级用户主题为系统主题
     *
     * @param themeId 主题id
     */
    void upgradeTheme(@Param("themeId") Long themeId);

    /**
     * 查询用户当前主题
     *
     * @param ownerId   用户id
     * @param themeType 主题类型
     * @return ThemeVo
     */
    ThemeVo selectUserCurrentTheme(@Param("ownerId") Long ownerId, @Param("themeType") ThemeType themeType);

    /**
     * 查询默认主题
     *
     * @param themeType 主题类型
     * @return ThemeVo
     */
    ThemeVo selectDefaultTheme(@Param("themeType") ThemeType themeType);
}
