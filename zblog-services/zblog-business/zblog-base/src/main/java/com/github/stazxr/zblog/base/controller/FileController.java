package com.github.stazxr.zblog.base.controller;

import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.router.Router;
import com.github.stazxr.zblog.bas.router.RouterLevel;
import com.github.stazxr.zblog.base.domain.bo.storage.BaseStorageConfig;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.core.annotation.ApiVersion;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 存储管理
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@Api(value = "FileController", tags = { "存储管理" })
public class FileController {
    private final FileService fileService;

    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return fileList
     */
    @GetMapping(value = "/pageFileList")
    @ApiOperation(value = "分页查询文件列表")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "分页查询文件列表", code = "FILEQ001")
    public PageInfo<FileVo> queryFileListByPage(FileQueryDto queryDto) {
        return fileService.queryFileListByPage(queryDto);
    }

    /**
     * 文件上传，支持单文件，多文件上传
     *
     * @param multipartFiles 多文件上传
     * @param multipartFile  单文件上传
     * @return List<UploadFileVo>
     * @throws Exception 文件上传失败
     */
    @Log
    @PostMapping("/uploadFile")
    @ApiOperation(value = "文件上传", notes = "只需要认证")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "文件上传", code = "FILEA001", level = RouterLevel.PUBLIC)
    public List<UploadFileVo> uploadFile(@RequestPart(value = "files", required = false) MultipartFile[] multipartFiles,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws Exception {
        return fileService.uploadFile(multipartFile, multipartFiles);
    }

    /**
     * 测试文件上传
     *
     * @param multipartFile 上传文件
     * @param uploadType    上传类型
     * @return UploadFileVo
     * @throws Exception 文件上传失败
     */
    @Log
    @PostMapping("/uploadFileTest")
    @ApiOperation(value = "测试文件上传")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "uploadType", value = "上传方式，1：本地存储、2：阿里云OSS、3：七牛云OSS", required = true, dataTypeClass = Integer.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "测试文件上传", code = "FILEA002")
    public UploadFileVo uploadFileTest(@RequestPart(value = "file") MultipartFile multipartFile, @RequestParam Integer uploadType) throws Exception {
        return fileService.uploadFileTest(multipartFile, uploadType);
    }

    /**
     * 下载文件
     *
     * @param fileId   文件id
     * @param isDown   是否强制下载
     * @param response 响应对象
     * @throws Exception 文件下载失败
     */
    @Log
    @GetMapping("/downloadFile")
    @ApiOperation(value = "下载文件", notes = "不需要token")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fileId", value = "文件id", required = true, dataTypeClass = Long.class),
        @ApiImplicitParam(name = "isDown", value = "是否强制下载", required = true, dataTypeClass = Boolean.class, example = "false")
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "下载文件", code = "FILEQ003", level = RouterLevel.OPEN)
    public void downloadFile(@RequestParam Long fileId, Boolean isDown, HttpServletResponse response) throws Exception {
        fileService.downloadFile(fileId, isDown, response);
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    @Log
    @PostMapping("/deleteFile")
    @ApiOperation(value = "删除文件", notes = "只需要认证")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "删除文件", code = "FILED001", level = RouterLevel.PUBLIC)
    public void deleteFile(@RequestParam Long fileId) {
        fileService.deleteFile(fileId);
    }

    /**
     * 测试文件删除
     *
     * @param fileId 文件id
     */
    @Log
    @PostMapping("/deleteFileTest")
    @ApiOperation(value = "测试文件删除")
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "测试文件删除", code = "FILED002")
    public void deleteFileTest(@RequestParam Long fileId) {
        fileService.deleteFile(fileId);
    }













    /**
     * 获取配置信息
     *
     * @param storageType 存储类型
     * @return Result
     */
    @GetMapping("/getConfigInfo")
    @ApiOperation(value = "获取配置信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "storageType", value = "上传方式，1：本地存储、2：阿里云OSS、3：七牛云OSS", required = true, dataTypeClass = Integer.class)
    })
    @ApiVersion(group = { BaseConst.ApiVersion.V_4_0_0 })
    @Router(name = "获取配置信息", code = "getStorageConfigInfo")
    public BaseStorageConfig getConfigInfo(@RequestParam Integer storageType) {
        return fileService.getConfigInfo(storageType);
    }
}
