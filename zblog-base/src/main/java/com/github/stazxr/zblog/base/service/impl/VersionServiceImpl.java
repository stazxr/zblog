package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.mapper.VersionMapper;
import com.github.stazxr.zblog.base.service.VersionService;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.util.Assert;
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

    /**
     * 分页查询版本列表
     *
     * @param queryDto 查询参数
     * @return versionList
     */
    @Override
    public PageInfo<Version> queryVersionListByPage(VersionQueryDto queryDto) {
        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(versionMapper.selectVersionList(queryDto));
    }

    /**
     * 新增版本
     *
     * @param version 版本
     */
    @Override
    public void addVersion(Version version) {
        version.setId(null);
        checkVersion(version);
        Assert.isTrue(versionMapper.insert(version) != 1, "新增失败");
    }

    /**
     * 编辑版本
     *
     * @param version 版本
     */
    @Override
    public void editVersion(Version version) {
        Assert.notNull(version.getId(), "参数【id】不能为空");
        checkVersion(version);
        Assert.isTrue(versionMapper.updateById(version) != 1, "修改失败");
    }

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     */
    @Override
    public void deleteVersion(Long versionId) {
        Assert.notNull(versionId, "参数【versionId】不能为空");
        Assert.isTrue(versionMapper.deleteById(versionId) != 1, "删除失败");
    }

    /**
     * 查询版本信息
     *
     * @param versionId 版本序列
     * @return 版本信息
     */
    @Override
    public Version queryVersionInfo(Long versionId) {
        Assert.notNull(versionId, "参数【versionId】不能为空");
        Version version = versionMapper.selectById(versionId);
        Assert.notNull(version, "查询版本信息失败，版本【" + versionId + "】不存在");
        return version;
    }

    private void checkVersion(Version version) {
        Assert.notNull(version.getVersionName(), "版本名称不能为空");

        // 检查版本名称是否存在
        Version dbVersion = versionMapper.selectByVersionName(version.getVersionName());
        DataValidated.isTrue(dbVersion != null && !dbVersion.getId().equals(version.getId()), "版本名称已存在");
    }
}
