package com.github.stazxr.zblog.content.ext.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileRelationMapper;
import com.github.stazxr.zblog.content.domain.enums.ServiceUploadBusinessType;
import com.github.stazxr.zblog.content.ext.converter.ThemeConverter;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemeDto;
import com.github.stazxr.zblog.content.ext.domain.dto.ThemeStatusDto;
import com.github.stazxr.zblog.content.ext.domain.dto.query.ThemeQueryDto;
import com.github.stazxr.zblog.content.ext.domain.entity.Theme;
import com.github.stazxr.zblog.content.ext.domain.enums.ThemeOwnerType;
import com.github.stazxr.zblog.content.ext.domain.error.ThemeErrorCode;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemePageVo;
import com.github.stazxr.zblog.content.ext.domain.vo.ThemeVo;
import com.github.stazxr.zblog.content.ext.mapper.ThemeMapper;
import com.github.stazxr.zblog.content.ext.mapper.ThemePageMapper;
import com.github.stazxr.zblog.content.ext.service.ThemeService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 主题管理业务实现层
 *
 * @author SunTao
 * @since 2026-06-14
 */
@Service
@RequiredArgsConstructor
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, Theme> implements ThemeService {
    private final ThemeConverter themeConverter;

    private final ThemePageMapper themePageMapper;

    private final FileMapper fileMapper;

    private final FileRelationMapper fileRelationMapper;

    /**
     * 分页查询主题列表
     *
     * @param queryDto 查询参数
     * @return IPage<ThemeVo>
     */
    @Override
    public IPage<ThemeVo> queryThemeListByPage(ThemeQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getThemeName())) {
            queryDto.setThemeName(queryDto.getThemeName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getOwnerName())) {
            queryDto.setOwnerName(queryDto.getOwnerName().trim());
        }

        // 分页查询
        Page<ThemeVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return baseMapper.selectThemeList(page, queryDto);
    }

    /**
     * 查询主题详情
     *
     * @param themeId 主题id
     * @return ThemeVo
     */
    @Override
    public ThemeVo queryThemeDetail(Long themeId) {
        ThemeVo themeVo = baseMapper.selectThemeDetail(themeId);
        if (themeVo != null) {
            List<ThemePageVo> pageList = themePageMapper.selectThemePage(themeId);
            themeVo.setPageList(pageList);
        }
        return ThrowUtils.requireNonNull(themeVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增主题
     *
     * @param themeDto 主题信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTheme(ThemeDto themeDto) {
        // 获取主题信息
        Theme theme = themeConverter.dtoToEntity(themeDto);
        // 新增时，不允许传入 ThemeId
        ThrowUtils.when(theme.getId() != null).system(BaseErrorCode.SCOREB001);
        // 主题信息检查
        checkTheme(theme);
        // 检查图片
        Long themeId = SequenceUtils.getId();
        if (themeDto.getPreviewCoverId() != null) {
            insertThemeImage(themeId, themeDto.getPreviewCoverId());
        }
        // 设置默认信息
        theme.setId(themeId);
        theme.setIsActive(false);
        theme.setIsDefault(false);
        theme.setOwnerType(ThemeOwnerType.USER);
        theme.setOwnerId(SecurityUtils.getLoginId());
        // 新增主题
        ThrowUtils.when(!save(theme)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑主题
     *
     * @param themeDto 主题信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTheme(ThemeDto themeDto) {
        // 获取主题信息
        Theme theme = themeConverter.dtoToEntity(themeDto);
        // 判断主题是否存在
        Theme dbTheme = baseMapper.selectById(theme.getId());
        ThrowUtils.throwIfNull(dbTheme, BaseErrorCode.ECOREA001);
        // 主题信息检查
        checkTheme(theme);
        // 检查图片
        if (themeDto.getPreviewCoverId() != null) {
            if (StringUtils.isBlank(dbTheme.getPreviewCover()) || !dbTheme.getPreviewCover().equals(theme.getPreviewCover())) {
                deleteThemePage(theme.getId());
                insertThemeImage(theme.getId(), themeDto.getPreviewCoverId());
            }
        } else {
            if (StringUtils.isBlank(themeDto.getPreviewCover())) {
                deleteThemePage(theme.getId());
            }
        }
        // 编辑主题
        theme.setUpdateUser(SecurityUtils.getLoginId());
        theme.setUpdateTime(DateUtils.formatNow(DateUtils.YMD_HMS_PATTERN));
        ThrowUtils.when(!baseMapper.updateThemeBaseInfo(theme)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 设置用户主题状态
     *
     * @param themeStatusDto 主题状态信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUserThemeStatus(ThemeStatusDto themeStatusDto) {
        // 判断主题是否存在
        Theme dbTheme = baseMapper.selectById(themeStatusDto.getThemeId());
        ThrowUtils.throwIfNull(dbTheme, BaseErrorCode.ECOREA001);
        // 只允许编辑用户主题状态
        ThrowUtils.throwIf(!ThemeOwnerType.USER.equals(dbTheme.getOwnerType()), ThemeErrorCode.ETHEMA002);
        // 只允许编辑自己的主题
        Long loginId = SecurityUtils.getLoginId();
        ThrowUtils.throwIf(!loginId.equals(dbTheme.getOwnerId()), ThemeErrorCode.ETHEMA004);
        if (themeStatusDto.getStatus()) {
            // 将主题修改为已激活状态，需要将当前激活的主题置为未激活状态，用户名下每个类别只能有一个激活态的主题
            baseMapper.inactiveUserTheme(dbTheme.getThemeType(), dbTheme.getOwnerId());
        }

        baseMapper.updateUserThemeStatus(themeStatusDto.getThemeId(), themeStatusDto.getStatus());
    }

    /**
     * 设置系统主题状态
     *
     * @param themeStatusDto 主题状态信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editSystemThemeStatus(ThemeStatusDto themeStatusDto) {
        // 判断主题是否存在
        Theme dbTheme = baseMapper.selectById(themeStatusDto.getThemeId());
        ThrowUtils.throwIfNull(dbTheme, BaseErrorCode.ECOREA001);
        // 只允许编辑系统主题状态
        ThrowUtils.throwIf(!ThemeOwnerType.SYSTEM.equals(dbTheme.getOwnerType()), ThemeErrorCode.ETHEMA003);
        if (themeStatusDto.getStatus()) {
            // 将主题修改为普通主题，系统主题每个类别下只能有一个默认主题
            baseMapper.inactiveSystemTheme(dbTheme.getThemeType());
        }

        baseMapper.updateSystemThemeStatus(themeStatusDto.getThemeId(), themeStatusDto.getStatus());
    }

    /**
     * 升级用户主题为系统主题
     *
     * @param themeId 主题id
     */
    @Override
    public void upgradeTheme(Long themeId) {
        // 判断主题是否存在
        Theme dbTheme = baseMapper.selectById(themeId);
        ThrowUtils.throwIfNull(dbTheme, BaseErrorCode.ECOREA001);
        // 只允许升级用户主题
        ThrowUtils.throwIf(!ThemeOwnerType.USER.equals(dbTheme.getOwnerType()), ThemeErrorCode.ETHEMA005);
        baseMapper.upgradeTheme(themeId);
    }

    /**
     * 删除主题
     *
     * @param themeId 主题id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTheme(Long themeId) {
        // 判断主题是否存在
        Theme dbTheme = baseMapper.selectById(themeId);
        ThrowUtils.throwIfNull(dbTheme, BaseErrorCode.ECOREA001);
        // 删除主题
        ThrowUtils.when(!removeById(themeId)).system(BaseErrorCode.SCOREA003);
        themePageMapper.deleteByThemeId(themeId);
    }

    private void checkTheme(Theme theme) {
        theme.setThemeName(theme.getThemeName().trim());
        ThrowUtils.when(checkThemeNameExist(theme)).service(ThemeErrorCode.ETHEMA001);
    }

    private void insertThemeImage(Long themeId, Long previewCoverId) {
        FileRelation fileRelation = new FileRelation();
        fileRelation.setFileId(previewCoverId);
        fileRelation.setBusinessId(themeId);
        fileRelation.setBusinessType(ServiceUploadBusinessType.THEME_IMG);
        fileRelationMapper.insert(fileRelation);
    }

    private void deleteThemePage(Long themeId) {
        fileMapper.deleteByBusinessInfo(themeId, ServiceUploadBusinessType.THEME_IMG);
        fileRelationMapper.deleteByBusinessInfo(themeId, ServiceUploadBusinessType.THEME_IMG);
    }

    private boolean checkThemeNameExist(Theme theme) {
        if (theme.getThemeName() != null) {
            LambdaQueryWrapper<Theme> queryWrapper = queryBuild().eq(Theme::getThemeName, theme.getThemeName());
            if (theme.getId() != null) {
                queryWrapper.ne(Theme::getId, theme.getId());
            }
            return baseMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Theme> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
