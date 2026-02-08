package com.github.stazxr.zblog.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 存储管理业务层
 *
 * @author SunTao
 * @since 2022-07-27
 */
public interface FileService {
    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return IPage<FileVo>
     */
    IPage<FileVo> queryFileListByPage(FileQueryDto queryDto);

    /**
     * 文件上传，支持单文件，多文件上传
     *
     * @param multipartFile  单文件上传
     * @param multipartFiles 多文件上传
     * @return List<UploadFileVo>
     * @throws Exception 文件上传失败
     */
    List<UploadFileVo> uploadFile(MultipartFile multipartFile, MultipartFile[] multipartFiles) throws Exception;

    /**
     * 测试文件上传
     *
     * @param multipartFile 上传文件
     * @param uploadType    上传类型
     * @return UploadFileVo
     * @throws Exception 文件上传失败
     */
    UploadFileVo uploadFileTest(MultipartFile multipartFile, Integer uploadType) throws Exception;

    /**
     * 下载文件
     *
     * @param fileId   文件id
     * @param isDown   是否强制下载
     * @param response 响应对象
     * @throws Exception 文件下载失败
     */
    void downloadFile(Long fileId, Boolean isDown, HttpServletResponse response) throws Exception;

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    void deleteFile(Long fileId);
}
