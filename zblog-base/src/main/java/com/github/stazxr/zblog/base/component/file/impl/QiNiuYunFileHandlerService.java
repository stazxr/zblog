package com.github.stazxr.zblog.base.component.file.impl;

import com.github.stazxr.zblog.base.component.file.FileTypeHandler;
import com.github.stazxr.zblog.base.component.file.model.FileInfo;
import com.github.stazxr.zblog.base.domain.bo.storage.QiNiuYunConfig;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.service.DictService;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.core.base.BaseConst;
import com.github.stazxr.zblog.core.enums.ResultCode;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.collection.CollectionUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import com.github.stazxr.zblog.util.qiniu.QiNiuPutRet;
import com.github.stazxr.zblog.util.qiniu.QiNiuYunOssConfig;
import com.github.stazxr.zblog.util.qiniu.QiNiuYunUtil;
import com.github.stazxr.zblog.util.secret.RsaUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 七牛云文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Component("QiNiuYunFileHandlerService")
@RequiredArgsConstructor
public class QiNiuYunFileHandlerService extends BaseFileService {
    private final DictService dictService;

    private final FileService fileService;

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
            QiNiuYunConfig config = getConfig();
            QiNiuYunOssConfig ossConfig = parseConfig(config);

            // 文件上传
            List<FileInfo> fileInfos = new ArrayList<>();
            for (MultipartFile file : files) {
                FileInfo fileInfo = parseMultiFile(file);

                String datePath = DateUtils.formatNow("/yyyy-MM/dd/");
                String filename = String.valueOf(GenerateIdUtils.getId()).concat(".").concat(fileInfo.getSuffix());
                String filePath = config.getPathPrefix().concat(datePath).concat(filename);
                QiNiuPutRet putRet = QiNiuYunUtil.uploadFile(ossConfig, ossConfig.getBucketName(), filePath, file.getBytes());
                fileInfo.setFileName(filename);
                fileInfo.setFilePath(config.getBucketName().concat(BUCKET_PATH_SPLIT_LABEL).concat(putRet.getKey()));
                fileInfo.setDownloadUrl(config.getDomain().concat("/").concat(putRet.getKey()));
                fileInfo.setUploadType(FileTypeHandler.QI_NIU_YUN.getType());
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
                QiNiuYunConfig config = getConfig();
                QiNiuYunOssConfig ossConfig = parseConfig(config);

                String[] tmpAry = filepath.split(BUCKET_PATH_SPLIT_LABEL);
                QiNiuYunUtil.deleteFile(ossConfig, tmpAry[0], tmpAry[1]);
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
        Map<String, List<String>> bucketList = new HashMap<>(CollectionUtils.mapSize(1));
        for (String filepath : filepathList) {
            if (filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
                String[] tmpAry = filepath.split(BUCKET_PATH_SPLIT_LABEL);
                List<String> keys = bucketList.getOrDefault(tmpAry[0], new ArrayList<>());
                keys.add(tmpAry[1]);
                bucketList.putIfAbsent(tmpAry[0], keys);
            }
        }

        try {
            // 获取云存储配置信息
            QiNiuYunConfig config = getConfig();
            QiNiuYunOssConfig ossConfig = parseConfig(config);

            for (String bucket : bucketList.keySet()) {
                String[] keys = bucketList.get(bucket).toArray(new String[bucketList.size()]);
                QiNiuYunUtil.batchDeleteFile(ossConfig, bucket, keys);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_DELETE_FAILED, e);
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
        try {
            File file = fileService.queryByFilepath(filepath);
            QiNiuYunUtil.downloadFile(file.getDownloadUrl(), response);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FILE_DOWNLOAD_FAILED, e);
        }
    }

    private QiNiuYunConfig getConfig() {
        String configValue = dictService.querySingleValue(BaseConst.DictKey.CLOUD_STORAGE_QI_NIU_CONFIG);
        return QiNiuYunConfig.instanceFromJson(configValue);
    }

    private QiNiuYunOssConfig parseConfig(QiNiuYunConfig config) throws Exception {
        if (StringUtils.hasBlank(config.getAk(), config.getSk(), config.getZone(), config.getBucketName())) {
            throw new ServiceException(ResultCode.BAD_CONFIGURATION, "请先完成云存储配置在进行操作");
        }

        String secret = config.getSk();
        Resource resource = new ClassPathResource("pri.key");
        String priKeyBase64 = FileUtils.readFileFromStream(resource.getInputStream());
        secret = RsaUtils.decryptByPrivateKey(priKeyBase64, secret);
        return QiNiuYunUtil.getOssConfig(config.getAk(), secret, config.getZone(), config.getBucketName());
    }
}
