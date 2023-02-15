package com.github.stazxr.zblog.base.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.bo.storage.BaseStorageConfig;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.base.domain.vo.ActiveStorageTypeVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * 附件上传业务
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileService extends IService<File> {
    /**
     * 获取默认文件上传类型
     *
     * @return FileTypeHandler.type
     */
    int getFileUploadType();

    /**
     * 批量插入文件
     *
     * @param uploadType 上传类型
     * @param fileList   文件列表
     * @return 上传文件列表
     */
    List<File> insertFile(String uploadType, List<FileInfo> fileList);

    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return FileList
     */
    PageInfo<File> queryFileListByPage(FileQueryDto queryDto);

    /**
     * 删除文件
     *
     * @param fileId 文件序号
     * @param businessId 业务序号
     */
    void deleteFile(Long fileId, Long businessId);

    /**
     * 测试文件删除
     *
     * @param fileIds 文件序号列表
     */
    void testDeleteFile(List<Long> fileIds);

    /**
     * 下载文件
     *
     * @param fileId   文件序号
     * @param isDown   是否强制下载
     * @param response 响应对象
     */
    void downloadFile(Long fileId, Boolean isDown, HttpServletResponse response);

    /**
     * 获取配置信息
     *
     * @param storageType 存储类型
     * @return BaseStorageConfig
     */
    BaseStorageConfig getConfigInfo(Integer storageType);

    /**
     * 保存配置信息
     *
     * @param param 配置信息
     */
    void setConfigInfo(JSONObject param);

    /**
     * 根据访问路径查询文件信息
     *
     * @param filepath 文件上传路径
     * @return File
     */
    File queryByFilepath(String filepath);

    /**
     * 激活存储配置
     *
     * @param storageType 存储类型
     */
    void activeStorageConfig(Integer storageType);

    /**
     * 获取激活的存储类型
     *
     * @return StorageType
     */
    ActiveStorageTypeVo getConfigStorageType();

    /**
     * 查询文件上传白名单
     *
     * @return fileSuffix
     */
    Set<String> getFileWhiteList();
}
