package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.base.converter.VersionConverter;
import com.github.stazxr.zblog.base.domain.dto.VersionDto;
import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.domain.error.VersionErrorCode;
import com.github.stazxr.zblog.base.domain.vo.VersionVo;
import com.github.stazxr.zblog.base.mapper.VersionMapper;
import com.github.stazxr.zblog.base.service.VersionService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 版本维护业务实现层
 *
 * @author SunTao
 * @since 2022-10-14
 */
@Service
@RequiredArgsConstructor
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {
    private final VersionMapper versionMapper;

    private final VersionConverter versionConverter;

    /**
     * 分页查询版本列表
     *
     * @param queryDto 查询参数
     * @return IPage<VersionVo>
     */
    @Override
    public IPage<VersionVo> queryVersionListByPage(VersionQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getVersionName())) {
            queryDto.setVersionName(queryDto.getVersionName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getUpdateContent())) {
            queryDto.setUpdateContent(queryDto.getUpdateContent().trim());
        }
        // 分页查询
        Page<VersionVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return versionMapper.selectVersionList(page, queryDto);
    }

    /**
     * 查询版本详情
     *
     * @param versionId 版本id
     * @return VersionVo
     */
    @Override
    public VersionVo queryVersionDetail(Long versionId) {
        VersionVo versionVo = versionMapper.selectVersionDetail(versionId);
        return ThrowUtils.requireNonNull(versionVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 新增版本
     *
     * @param versionDto 版本
     */
    @Override
    public void addVersion(VersionDto versionDto) {
        // 获取版本信息
        Version version = versionConverter.dtoToEntity(versionDto);
        // 新增时，不允许传入 VersionId
        ThrowUtils.when(version.getId() != null).system(BaseErrorCode.SCOREB001);
        // 版本信息检查
        checkVersion(version);
        // 新增版本
        ThrowUtils.when(!save(version)).system(BaseErrorCode.SCOREA001);
    }

    /**
     * 编辑版本
     *
     * @param versionDto 版本
     */
    @Override
    public void editVersion(VersionDto versionDto) {
        // 获取版本信息
        Version version = versionConverter.dtoToEntity(versionDto);
        // 判断版本是否存在
        Version dbVersion = versionMapper.selectById(version.getId());
        ThrowUtils.requireNonNull(dbVersion, BaseErrorCode.ECOREA001);
        // 版本信息检查
        checkVersion(version);
        // 编辑版本
        ThrowUtils.when(!updateById(version)).system(BaseErrorCode.SCOREA002);
    }

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     */
    @Override
    public void deleteVersion(Long versionId) {
        // 判断版本是否存在
        Version dbVersion = versionMapper.selectById(versionId);
        ThrowUtils.requireNonNull(dbVersion, BaseErrorCode.ECOREA001);
        // 删除版本
        ThrowUtils.when(!removeById(versionId)).system(BaseErrorCode.SCOREA003);
    }

    private void checkVersion(Version version) {
        // 检查版本名称是否存在
        ThrowUtils.throwIf(checkVersionNameExist(version), VersionErrorCode.EVERSA000);
    }

    private boolean checkVersionNameExist(Version version) {
        if (version.getVersionName() != null) {
            LambdaQueryWrapper<Version> queryWrapper = queryBuild().eq(Version::getVersionName, version.getVersionName());
            if (version.getId() != null) {
                queryWrapper.ne(Version::getId, version.getId());
            }
            return versionMapper.exists(queryWrapper);
        }
        return false;
    }

    private LambdaQueryWrapper<Version> queryBuild() {
        return Wrappers.lambdaQuery();
    }
}
