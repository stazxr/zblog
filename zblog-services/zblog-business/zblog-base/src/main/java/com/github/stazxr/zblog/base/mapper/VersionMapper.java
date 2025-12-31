package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.VersionQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Version;
import com.github.stazxr.zblog.base.domain.vo.VersionVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 版本维护数据层
 *
 * @author SunTao
 * @since 2022-10-14
 */
public interface VersionMapper extends BaseMapper<Version> {
    /**
     * 查询版本列表
     *
     * @param queryDto 查询参数
     * @return versionList
     */
    List<VersionVo> selectVersionList(VersionQueryDto queryDto);

    /**
     * 查询版本详情
     *
     * @param versionId 版本id
     * @return VersionVo
     */
    VersionVo selectVersionDetail(@Param("versionId") Long versionId);
}
