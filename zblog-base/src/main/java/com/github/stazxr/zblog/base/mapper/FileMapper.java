package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件上传数据处理
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
    List<File> selectFileList(FileQueryDto queryDto);

    /**
     * 根据 businessId 批量删除文件
     *
     * @param deleteUser 删除用户
     * @param businessId 业务序列
     */
    void deleteFileByBusinessId(@Param("deleteUser") String deleteUser, @Param("businessId") Long businessId);

    /**
     * 根据 fileId 删除文件
     *
     * @param deleteUser 删除用户
     * @param fileId     文件序列
     */
    void deleteFileByFileId(@Param("deleteUser") String deleteUser, @Param("fileId") Long fileId);

    /**
     * 根据 fileId 查询文件路径列表
     *
     * @param fileId 文件序列
     * @return filepathList
     */
    List<String> selectFilePathListByFileId(@Param("fileId") Long fileId);

    /**
     * 根据 businessId 查询文件路径列表
     *
     * @param businessId 业务序列
     * @return filepathList
     */
    List<String> selectFilePathListByBusinessId(@Param("businessId") Long businessId);

    /**
     * 根据访问路径查询文件信息
     *
     * @param filepath 文件上传路径
     * @return File
     */
    File selectByFilepath(@Param("filepath") String filepath);
}
