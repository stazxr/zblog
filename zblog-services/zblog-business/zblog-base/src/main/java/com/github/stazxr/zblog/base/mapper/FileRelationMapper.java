package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 附件上传中间数据处理
 *
 * @author SunTao
 * @since 2022-10-21
 */
public interface FileRelationMapper extends BaseMapper<FileRelation> {
    /**
     * 根据业务ID删除文件中间数据
     *
     * @param businessId 业务ID
     */
    void deleteByBusinessId(@Param("businessId") Long businessId);

    /**
     * 查询业务对应的文件列表
     *
     * @param businessId 业务ID
     * @return FileList
     */
    List<File> selectByBusinessId(@Param("businessId") Long businessId);
}
