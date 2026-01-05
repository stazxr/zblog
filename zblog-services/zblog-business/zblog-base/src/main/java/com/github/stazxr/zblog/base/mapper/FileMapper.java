package com.github.stazxr.zblog.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 逻辑文件数据层
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileMapper extends BaseMapper<File> {
    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return FileList
     */
    List<FileVo> selectFileList(FileQueryDto queryDto);

    /**
     * 根据文件id查询文件信息
     *
     * @param fileId 文件id
     * @return FileVo
     */
    FileVo selectFileDetailById(@Param("fileId") Long fileId);

    /**
     * 根据业务信息删除逻辑文件信息
     *
     * @param businessId   业务id
     * @param businessType 业务类型
     */
    void deleteByBusinessInfo(@Param("businessId") Long businessId, @Param("businessType") Integer businessType);
}
