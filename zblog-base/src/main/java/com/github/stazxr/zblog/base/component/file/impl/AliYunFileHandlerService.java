package com.github.stazxr.zblog.base.component.file.impl;

import com.github.stazxr.zblog.base.component.file.FileTypeHandler;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.base.domain.bo.storage.AliYunConfig;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.ali.AliYunOssConfig;
import com.github.stazxr.zblog.util.ali.AliYunUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.secret.RsaUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Component("AliYunFileHandlerService")
@RequiredArgsConstructor
public class AliYunFileHandlerService extends BaseFileService {
    private final DictService dictService;

    /**
     * 文件上传
     *
     * @param files 文件列表
     * @return 上传的文件列表
     */
    @Override
    public List<FileInfo> uploadFile(MultipartFile[] files) {
        try {
            // 获取云存储配置信息
            AliYunConfig config = getConfig();
            AliYunOssConfig ossConfig = parseConfig(config);

            // 文件上传
            List<FileInfo> fileInfos = new ArrayList<>();
            for (MultipartFile file : files) {
                FileInfo fileInfo = parseMultiFile(file);

                String datePath = DateUtils.formatNow("/yyyy-MM/dd/");
                String filename = String.valueOf(GenerateIdUtils.getId()).concat(".").concat(fileInfo.getSuffix());
                String filePath = config.getPathPrefix().concat(datePath).concat(filename);
                AliYunUtils.uploadFile(ossConfig, ossConfig.getBucketName(), filePath, file.getBytes());
                fileInfo.setFileName(filename);
                fileInfo.setFilePath(config.getBucketName().concat(BUCKET_PATH_SPLIT_LABEL).concat(filePath));
                fileInfo.setDownloadUrl(config.getDomain().concat("/").concat(filePath));
                fileInfo.setUploadType(FileTypeHandler.ALI_YUN.getType());
                fileInfos.add(fileInfo);
            }
            return fileInfos;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 删除文件
     *
     * @param filepath 文件路径
     */
    @Override
    public void deleteFile(String filepath) {
        if (StringUtils.isNotBlank(filepath) && filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
            try {
                // 获取云存储配置信息
                AliYunConfig config = getConfig();
                AliYunOssConfig ossConfig = parseConfig(config);

                String[] tmpAry = filepath.split(BUCKET_PATH_SPLIT_LABEL);
                AliYunUtils.deleteFile(ossConfig, tmpAry[0], tmpAry[1]);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException(ResultCode.FILE_DELETE_FAILED, e);
            }
        }
    }

    /**
     * 批量删除文件
     *
     * @param filepathList 文件路径列表
     */
    @Override
    public void deleteFiles(List<String> filepathList) {
        if (filepathList != null && filepathList.size() > 0) {
            for (String filepath : filepathList) {
                deleteFile(filepath);
            }
        }
    }

    /**
     * 下载文件
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @Override
    public void downloadFile(String filepath, HttpServletResponse response) {
        if (StringUtils.isNotBlank(filepath) && filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
            try {
                // 获取云存储配置信息
                AliYunConfig config = getConfig();
                AliYunOssConfig ossConfig = parseConfig(config);

                String[] tmpAry = filepath.split(BUCKET_PATH_SPLIT_LABEL);
                AliYunUtils.downloadFile(ossConfig, tmpAry[0], tmpAry[1], response);
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceException(ResultCode.FILE_DOWNLOAD_FAILED, e);
            }
        }
    }

    private AliYunConfig getConfig() {
        String configValue = dictService.querySingleValue(BaseConst.DictKey.CLOUD_STORAGE_A_LI_CONFIG);
        return AliYunConfig.instanceFromJson(configValue);
    }

    private AliYunOssConfig parseConfig(AliYunConfig config) throws Exception {
        if (StringUtils.hasBlank(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret(), config.getBucketName())) {
            throw new ServiceException(ResultCode.BAD_CONFIGURATION, "请先完成云存储配置在进行操作");
        }

        String secret = config.getAccessKeySecret();
        Resource resource = new ClassPathResource("pri.key");
        String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
        secret = RsaUtils.decryptByPrivateKey(priKeyBase64, secret);
        return AliYunUtils.getOssConfig(config.getEndpoint(), config.getAccessKeyId(), secret, config.getBucketName());
    }
}
