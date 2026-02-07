package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.stazxr.zblog.base.domain.dto.VersionDto;
import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.domain.vo.VersionVo;

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
     * @return IPage<VersionVo>
     */
    IPage<VersionVo> queryVersionListByPage(VersionQueryDto queryDto);

    /**
     * 查询版本详情
     *
     * @param versionId 版本id
     * @return VersionVo
     */
    VersionVo queryVersionDetail(Long versionId);

    /**
     * 新增版本
     *
     * @param versionDto 版本
     */
    void addVersion(VersionDto versionDto);

    /**
     * 编辑版本
     *
     * @param versionDto 版本
     */
    void editVersion(VersionDto versionDto);

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     */
    void deleteVersion(Long versionId);
}
