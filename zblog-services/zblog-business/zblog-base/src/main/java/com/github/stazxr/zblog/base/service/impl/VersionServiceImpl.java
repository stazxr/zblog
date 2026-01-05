package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.converter.VersionConverter;
import com.github.stazxr.zblog.base.domain.dto.VersionDto;
import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.domain.vo.VersionVo;
import com.github.stazxr.zblog.base.mapper.VersionMapper;
import com.github.stazxr.zblog.base.service.VersionService;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @return PageInfo<VersionVo>
     */
    @Override
    public PageInfo<VersionVo> queryVersionListByPage(VersionQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        if (StringUtils.isNotBlank(queryDto.getVersionName())) {
            queryDto.setVersionName(queryDto.getVersionName().trim());
        }
        if (StringUtils.isNotBlank(queryDto.getUpdateContent())) {
            queryDto.setUpdateContent(queryDto.getUpdateContent().trim());
        }
        // 分页查询
        try (Page<VersionVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<VersionVo> dataList = versionMapper.selectVersionList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 查询版本详情
     *
     * @param versionId 版本id
     * @return VersionVo
     */
    @Override
    public VersionVo queryVersionDetail(Long versionId) {
        Assert.notNull(versionId, ExpMessageCode.of("valid.common.id.NotNull"));
        VersionVo versionVo = versionMapper.selectVersionDetail(versionId);
        Assert.notNull(versionVo, ExpMessageCode.of("valid.common.data.notFound"));
        return versionVo;
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
        Assert.isNull(version.getId(), ExpMessageCode.of("valid.common.addWithIdError"));
        // 版本信息检查
        checkVersion(version);
        // 新增版本
        Assert.affectOneRow(versionMapper.insert(version), ExpMessageCode.of("result.common.add.failed"));
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
        Assert.notNull(dbVersion, ExpMessageCode.of("valid.common.data.notFound"));
        // 版本信息检查
        checkVersion(version);
        // 编辑版本
        Assert.affectOneRow(versionMapper.updateById(version), ExpMessageCode.of("result.common.edit.failed"));
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
        Assert.notNull(dbVersion, ExpMessageCode.of("valid.common.data.notFound"));
        // 删除版本
        Assert.affectOneRow(versionMapper.deleteById(versionId), ExpMessageCode.of("result.common.delete.failed"));
    }

    private void checkVersion(Version version) {
        // 检查版本名称是否存在
        Assert.failIfTrue(checkVersionNameExist(version), ExpMessageCode.of("valid.version.versionName.exist"));
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
