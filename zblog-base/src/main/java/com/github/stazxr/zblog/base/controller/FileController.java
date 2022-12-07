package com.github.stazxr.zblog.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.stazxr.zblog.base.component.file.FileHandler;
import com.github.stazxr.zblog.base.component.file.FileTypeHandler;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.base.component.file.model.FileUploadType;
import com.github.stazxr.zblog.base.domain.dto.FileDeleteDto;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.core.annotation.RequestPostSingleParam;
import com.github.stazxr.zblog.core.annotation.Router;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.model.Result;
import com.github.stazxr.zblog.log.annotation.Log;
import com.github.stazxr.zblog.util.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 存储管理
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
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return fileList
     */
    @GetMapping(value = "/queryFileListByPage")
    @Router(name = "分页查询文件列表", code = "queryFileListByPage")
    public Result queryFileListByPage(FileQueryDto queryDto) {
        return Result.success().data(fileService.queryFileListByPage(queryDto));
    }

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
        Assert.isTrue((files == null || files.length < 1) && file == null, "上传失败，待上传文件列表为空");
        if (files == null || files.length < 1) {
            files = new MultipartFile[] {file};
        }

        // 获取文件存储类型
        List<FileInfo> fileList = new ArrayList<>();
        int fileUploadType = fileService.getFileUploadType();
        FileHandler fileHandler = FileTypeHandler.instance(fileUploadType);

        // 白名单检查
        whiteListPreCheck(files);

        try {
            // 上传文件
            fileList = fileHandler.uploadFile(files);
            return Result.success("上传成功").data(fileService.insertFile(FileUploadType.NORMAL.getType(), fileList));
        } catch (Exception e) {
            try {
                // 上传失败，可能是数据入库失败，删除已上传的文件
                List<String> filePaths = fileList.stream().map(FileInfo::getFilePath).collect(Collectors.toList());
                fileHandler.deleteFiles(filePaths);
            } catch (Exception ignored) {
            }

            throw new ServiceException(ResultCode.FILE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 下载文件
     *
     * @param fileId   文件序号
     * @param isDown   是否强制下载
     * @param response 响应对象
     * @return Result
     */
    @GetMapping("/downloadFile")
    @Router(name = "下载文件", code = "downloadFile", level = BaseConst.PermLevel.OPEN)
    public Result downloadFile(Long fileId, Boolean isDown, HttpServletResponse response) {
        fileService.downloadFile(fileId, isDown, response);
        return Result.success("下载成功");
    }

    /**
     * 删除文件
     *
     * @param paramDto 参数信息
     * @return Result
     */
    @PostMapping("/deleteFile")
    @Router(name = "删除文件", code = "deleteFile", level = BaseConst.PermLevel.PUBLIC)
    public Result deleteFile(@RequestBody FileDeleteDto paramDto) {
        fileService.deleteFile(paramDto.getFileId(), paramDto.getBusinessId());
        return Result.success("删除成功");
    }

    /**
     * 测试文件上传
     *
     * @param file  单文件上传
     * @param type  上传类型
     * @return FileUploadVo List
     */
    @Log
    @PostMapping("/testUploadFile")
    @Router(name = "测试文件上传", code = "testUploadFile")
    public Result testUploadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam Integer type) {
        Assert.notNull(type, "参数【type】不能为空".toLowerCase(Locale.ROOT));
        Assert.isTrue(file == null, "上传失败，待上传文件列表为空");

        try {
            // 上传文件
            FileHandler fileHandler = FileTypeHandler.of(type);
            List<FileInfo> fileList = fileHandler.uploadFile(new MultipartFile[] {file});
            return Result.success("上传成功").data(fileService.insertFile(FileUploadType.TEST.getType(), fileList));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 测试文件删除
     *
     * @param fileIds 文件序号列表
     * @return Result
     */
    @Log
    @PostMapping("/testDeleteFile")
    @Router(name = "测试文件删除", code = "testDeleteFile")
    public Result testDeleteFile(@RequestBody List<Long> fileIds) {
        fileService.testDeleteFile(fileIds);
        return Result.success();
    }

    /**
     * 获取配置信息
     *
     * @param storageType 存储类型
     * @return Result
     */
    @GetMapping("/getConfigInfo")
    @Router(name = "获取配置信息", code = "getStorageConfigInfo")
    public Result getConfigInfo(@RequestParam Integer storageType) {
        return Result.success().data(fileService.getConfigInfo(storageType));
    }

    /**
     * 保存配置信息
     *
     * @param param 配置信息
     * @return Result
     */
    @Log
    @PostMapping("/setConfigInfo")
    @Router(name = "保存配置信息", code = "setStorageConfigInfo")
    public Result setConfigInfo(@RequestBody JSONObject param) {
        fileService.setConfigInfo(param);
        return Result.success();
    }

    /**
     * 获取激活的存储类型
     *
     * @return StorageType
     */
    @GetMapping("/getConfigStorageType")
    @Router(name = "获取激活的存储类型", code = "getConfigStorageType")
    public Result getConfigStorageType() {
        return Result.success().data(fileService.getConfigStorageType());
    }

    /**
     * 激活存储配置
     *
     * @param storageType 存储类型
     * @return Result
     */
    @Log
    @PostMapping("/activeStorageConfig")
    @Router(name = "激活存储配置", code = "activeStorageConfig")
    public Result activeStorageConfig(@RequestPostSingleParam Integer storageType) {
        fileService.activeStorageConfig(storageType);
        return Result.success();
    }

    private void whiteListPreCheck(MultipartFile[] files) {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {

            }
        }
    }
}
