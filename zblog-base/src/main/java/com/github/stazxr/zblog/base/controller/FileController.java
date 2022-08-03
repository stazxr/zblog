package com.github.stazxr.zblog.base.controller;

import com.github.stazxr.zblog.base.third.file.FileHandler;
import com.github.stazxr.zblog.base.third.file.FileTypeHandler;
import com.github.stazxr.zblog.base.third.file.model.FileInfo;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 附件上传管理
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 文件上传类型
     */
    @Value("${zblog.file-upload-type:1}")
    private int fileUploadType;

    /**
     * 文件上传，支持单文件，多文件上传
     *
     * @param files 多文件上传
     * @param file 单文件上传
     * @return FileUploadVo List
     */
    @PostMapping("/uploadFile")
    @Router(name = "文件上传", code = "uploadFile", level = BaseConst.PermLevel.PUBLIC)
    public Result uploadFile(@RequestPart(value = "files", required = false) MultipartFile[] files,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        boolean isMulFileEmpty = files == null || files.length < 1;
        if (isMulFileEmpty && file == null) {
            throw new ServiceException(ResultCode.FILE_UPLOAD_FAILED, "文件上传失败，参数【files, file】为空");
        }

        // 设置上传文件列表
        if (files == null || files.length < 1) {
            files = new MultipartFile[] {file};
        }

        // 获取上传接口
        List<FileInfo> fileList = new ArrayList<>();
        FileHandler fileHandler = FileTypeHandler.instance(fileUploadType);

        try {
            // 上传文件
            fileList = fileHandler.uploadFile(files);
            return Result.success("上传成功").data(fileService.insertFile(fileList, fileUploadType));
        } catch (Exception e) {
            // fileHandler.deleteFile(fileList);
            throw new ServiceException(ResultCode.FILE_UPLOAD_FAILED, e);
        }
    }
}
