package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;

/**
 * 版本维护服务层
 *
 * @author SunTao
 * @since 2022-10-14
 */
public interface VersionService extends IService<Version> {
    /**
     * 分页查询版本列表
     *
     * @param queryDto 查询参数
     * @return versionList
     */
    PageInfo<Version> queryVersionListByPage(VersionQueryDto queryDto);

    /**
     * 新增版本
     *
     * @param version 版本
     */
    void addVersion(Version version);

    /**
     * 编辑版本
     *
     * @param version 版本
     */
    void editVersion(Version version);

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     */
    void deleteVersion(Long versionId);

    /**
     * 查询版本信息
     *
     * @param versionId 版本序列
     * @return 版本信息
     */
    Version queryVersionInfo(Long versionId);
}
