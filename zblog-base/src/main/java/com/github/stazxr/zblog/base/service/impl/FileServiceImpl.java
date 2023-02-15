package com.github.stazxr.zblog.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.component.file.FileHandler;
import com.github.stazxr.zblog.base.component.file.FileTypeHandler;
import com.github.stazxr.zblog.base.component.file.model.FileUploadType;
import com.github.stazxr.zblog.base.domain.bo.storage.AliYunConfig;
import com.github.stazxr.zblog.base.domain.bo.storage.BaseStorageConfig;
import com.github.stazxr.zblog.base.domain.bo.storage.LocalConfig;
import com.github.stazxr.zblog.base.domain.bo.storage.QiNiuYunConfig;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.domain.vo.ActiveStorageTypeVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.config.properties.ZblogProperties;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.BadConfigurationException;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.secret.RsaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 附件上传业务处理
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    private final DictMapper dictMapper;

    private final FileMapper fileMapper;

    private final ZblogProperties zblogProperties;

    /**
     * 文件上传类型
     */
    @Value("${zblog.file-upload-type:1}")
    private int fileUploadType;

    /**
     * 获取默认文件上传类型
     *
     * @return FileTypeHandler.type
     */
    @Override
    public int getFileUploadType() {
        String dictType = dictMapper.selectSingleValue(BaseConst.DictKey.ACTIVE_UPLOAD_TYPE);
        if (StringUtils.isNotBlank(dictType) && !String.valueOf(FileTypeHandler.DEFAULT.getType()).equals(dictType)) {
            return Integer.parseInt(dictType);
        }

        return fileUploadType;
    }

    /**
     * 批量插入文件
     *
     * @param uploadType 上传类型
     * @param fileList   文件列表
     * @return 上传文件列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<File> insertFile(String uploadType, List<FileInfo> fileList) {
        List<File> result = new ArrayList<>();
        for (FileInfo fileInfo : fileList) {
            File file = new File();
            Long fileId = GenerateIdUtils.getId();
            file.setId(fileId);
            file.setFilename(fileInfo.getFileName());
            file.setOriginalFilename(fileInfo.getOriginalFileName());
            file.setFilePath(fileInfo.getFilePath());
            file.setDownloadUrl(fileInfo.getDownloadUrl());
            file.setMd5(fileInfo.getMd5());
            file.setSize(FileUtils.parseSize(fileInfo.getSize()));
            file.setFileType(FileUtils.getFileType(fileInfo.getSuffix()));
            file.setStorageType(fileInfo.getUploadType());
            file.setUploadType(uploadType);
            Assert.isTrue(fileMapper.insert(file) != 1, "文件信息入库失败");
            result.add(fileMapper.selectById(fileId));
        }

        return result;
    }

    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return FileList
     */
    @Override
    public PageInfo<File> queryFileListByPage(FileQueryDto queryDto) {
        queryDto.checkPage();

        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(fileMapper.selectFileList(queryDto));
    }

    /**
     * 删除文件
     *
     * @param fileId     文件序号
     * @param businessId 业务序号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long fileId, Long businessId) {
        Assert.isTrue(fileId == null && businessId == null, "参数为空");
        String deleteUser = SecurityUtils.getLoginUsername();

        // 删除数据
        List<String> filepathList;
        if (fileId != null) {
            filepathList = fileMapper.selectFilePathListByFileId(fileId);
            fileMapper.deleteFileByFileId(deleteUser, fileId);
        } else {
            filepathList = fileMapper.selectFilePathListByBusinessId(businessId);
            fileMapper.deleteFileByBusinessId(deleteUser, businessId);
        }

        // 删除文件
        FileHandler fileHandler = FileTypeHandler.instance(getFileUploadType());
        fileHandler.deleteFiles(filepathList);
    }

    /**
     * 测试文件删除
     *
     * @param fileIds 文件序号列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testDeleteFile(List<Long> fileIds) {
        String deleteUser = SecurityUtils.getLoginUsername();
        if (fileIds != null && fileIds.size() > 0) {
            Map<Integer, List<String>> fileMap = new HashMap<>(CollectionUtils.mapSize(3));
            for (Long fileId : fileIds) {
                File file = fileMapper.selectById(fileId);
                if (file == null) {
                    continue;
                }

                if (FileUploadType.NORMAL.getType().equals(file.getUploadType())) {
                    throw new ServiceException("文件" + file.getOriginalFilename() + "为普通文件，不允许删除");
                }

                // 待删除文件列表
                List<String> filepathList = fileMapper.selectFilePathListByFileId(fileId);
                List<String> existList = fileMap.getOrDefault(file.getStorageType(), new ArrayList<>());
                existList.addAll(filepathList);
                fileMap.putIfAbsent(file.getStorageType(), existList);

                // 删除数据
                fileMapper.deleteFileByFileId(deleteUser, fileId);
            }

            // 删除文件
            for (Integer type : fileMap.keySet()) {
                FileHandler fileHandler = FileTypeHandler.instance(type);
                fileHandler.deleteFiles(fileMap.getOrDefault(type, new ArrayList<>()));
            }
        }
    }

    /**
     * 下载文件
     *
     * @param fileId   文件序号
     * @param isDown   是否强制下载
     * @param response 响应对象
     */
    @Override
    public void downloadFile(@RequestParam Long fileId, Boolean isDown, HttpServletResponse response) {
        Assert.notNull(fileId, "参数【fileId】不能为空");
        File file = fileMapper.selectById(fileId);
        Assert.notNull(file, "文件不存在");

        try {
            if (isDown == null || !isDown) {
                // inline
                response.addHeader("Content-Disposition", "inline;fileName=" + '"' + file.getOriginalFilename() + '"');
            } else {
                // download
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
            }

            // 下载文件
            FileHandler fileHandler = FileTypeHandler.instance(file.getStorageType());
            fileHandler.downloadFile(file.getFilePath(), response);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_DOWNLOAD_FAILED, e);
        }
    }

    /**
     * 获取配置信息
     *
     * @param storageType 存储类型
     * @return BaseStorageConfig
     */
    @Override
    public BaseStorageConfig getConfigInfo(Integer storageType) {
        Assert.notNull(storageType, "参数【storageType】不能为空");
        if (FileTypeHandler.LOCAL.getType() == storageType) {
            // 本地存储配置从配置文件获取
            LocalConfig config = new LocalConfig();
            config.setDomain(zblogProperties.getFileBaseUrl());
            config.setUploadFolder(zblogProperties.getFileUploadPath());
            return config;
        } else if (FileTypeHandler.ALI_YUN.getType() == storageType) {
            String configValue = dictMapper.selectSingleValue(BaseConst.DictKey.CLOUD_STORAGE_A_LI_CONFIG);
            AliYunConfig config = AliYunConfig.instanceFromJson(configValue);
            config.setAccessKeySecret(decodeSecret(config.getAccessKeySecret()));
            return config;
        } else if (FileTypeHandler.QI_NIU_YUN.getType() == storageType) {
            String configValue = dictMapper.selectSingleValue(BaseConst.DictKey.CLOUD_STORAGE_QI_NIU_CONFIG);
            QiNiuYunConfig config = QiNiuYunConfig.instanceFromJson(configValue);
            config.setSk(decodeSecret(config.getSk()));
            return config;
        } else {
            throw new BadConfigurationException("不支持的存储类型：" + storageType);
        }
    }

    /**
     * 保存配置信息
     *
     * @param param 配置信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setConfigInfo(JSONObject param) {
        Integer storageType = param.getInteger("storageType");
        Assert.notNull(storageType, "参数【storageType】不能为空");

        param.remove("storageType");
        if (FileTypeHandler.LOCAL.getType() == storageType) {
            dictMapper.updateSingleValue(BaseConst.DictKey.CLOUD_STORAGE_LOCAL_CONFIG, param.toJSONString());
        } else if (FileTypeHandler.ALI_YUN.getType() == storageType) {
            dictMapper.updateSingleValue(BaseConst.DictKey.CLOUD_STORAGE_A_LI_CONFIG, param.toJSONString());
        } else if (FileTypeHandler.QI_NIU_YUN.getType() == storageType) {
            dictMapper.updateSingleValue(BaseConst.DictKey.CLOUD_STORAGE_QI_NIU_CONFIG, param.toJSONString());
        } else {
            throw new BadConfigurationException("不支持的存储类型：" + storageType);
        }
    }

    /**
     * 根据访问路径查询文件信息
     *
     * @param filepath 文件上传路径
     * @return File
     */
    @Override
    public File queryByFilepath(String filepath) {
        Assert.notNull(filepath, "参数【filepath】不能为空");
        return fileMapper.selectByFilepath(filepath);
    }

    /**
     * 激活存储配置
     *
     * @param storageType 存储类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeStorageConfig(Integer storageType) {
        Assert.notNull(storageType, "参数【storageType】不能为空");
        dictMapper.updateSingleValue(BaseConst.DictKey.ACTIVE_UPLOAD_TYPE, String.valueOf(storageType));
    }

    /**
     * 获取激活的存储类型
     *
     * @return StorageType
     */
    @Override
    public ActiveStorageTypeVo getConfigStorageType() {
        ActiveStorageTypeVo typeVo = new ActiveStorageTypeVo();
        typeVo.setDefaultType(fileUploadType);
        typeVo.setDefaultTypeName(FileTypeHandler.ofName(fileUploadType));

        // 激活的存储方式
        String configValue = dictMapper.selectSingleValue(BaseConst.DictKey.ACTIVE_UPLOAD_TYPE);
        if (StringUtils.isBlank(configValue) || String.valueOf(FileTypeHandler.DEFAULT.getType()).equals(configValue)) {
            typeVo.setActiveType(FileTypeHandler.DEFAULT.getType());
            typeVo.setActiveTypeName(FileTypeHandler.ofName(FileTypeHandler.DEFAULT.getType()));
        } else {
            typeVo.setActiveType(Integer.valueOf(configValue));
            typeVo.setActiveTypeName(FileTypeHandler.ofName(Integer.parseInt(configValue)));
        }

        return typeVo;
    }

    /**
     * 查询文件上传白名单
     *
     * @return fileSuffix
     */
    @Override
    public Set<String> getFileWhiteList() {
        List<Dict> dicts = dictMapper.selectItems(BaseConst.DictKey.FILE_UPLOAD_WHITE_LIST);
        return dicts.stream().map(Dict::getValue).collect(Collectors.toSet());
    }

    private String decodeSecret(String secret) {
        if (StringUtils.isNotBlank(secret)) {
            try {
                // encrypt
                Resource resource = new ClassPathResource("pri.key");
                String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
                return RsaUtils.decryptByPrivateKey(priKeyBase64, secret);
            } catch (Exception e) {
                log.error("获取存储配置时，decodeSecret失败: {}", secret, e);
            }
        }

        return "";
    }
}
