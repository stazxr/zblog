package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.base.third.file.model.FileInfo;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件上传业务处理
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Resource
    private FileMapper fileMapper;

    /**
     * 批量插入文件
     *
     * @param fileList 文件列表
     * @param fileUploadType 上传方式
     * @return 上传文件列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<File> insertFile(List<FileInfo> fileList, int fileUploadType) {
        List<File> result = new ArrayList<>();
        for (FileInfo fileInfo : fileList) {
            File file = new File();
            file.setId(GenerateIdUtils.getId());
            file.setFilename(fileInfo.getFileName());
            file.setOriginalFilename(fileInfo.getOriginalFileName());
            file.setFilePath(fileInfo.getFilePath());
            file.setDownloadUrl(fileInfo.getDownloadUrl());
            file.setMd5(fileInfo.getMd5());
            file.setSize(FileUtils.parseSize(fileInfo.getSize()));
            file.setFileType(FileUtils.getFileType(fileInfo.getSuffix()));
            file.setStorageType(String.valueOf(fileUploadType));
            fileMapper.insert(file);
            result.add(file);
        }

        return result;
    }
}
