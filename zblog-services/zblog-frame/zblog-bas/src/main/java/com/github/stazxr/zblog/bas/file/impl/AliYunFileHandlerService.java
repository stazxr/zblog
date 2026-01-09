package com.github.stazxr.zblog.bas.file.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * 阿里云文件存储
 *
 * @author SunTao
 * @since 2022-07-27
 */
public class AliyunFileHandlerService extends BaseFileService {
    /**
     * 域名
     */
    private final String baseUrl;

    /**
     * 上传路径
     */
    private final String fileUploadPath;

    /**
     * 桶名称
     */
    private final String bucketName;

    private final OSS ossClient;

    public AliyunFileHandlerService(FileProperties.AliyunConfig config) {
        // 确保路径以斜杠结尾
        String baseUrl = config.getBaseUrl();
        if (!baseUrl.endsWith(PATH_SEPARATOR)) {
            baseUrl = baseUrl + PATH_SEPARATOR;
        }
        String fileUploadPath = config.getFileUploadPath();
        if (!fileUploadPath.endsWith(PATH_SEPARATOR)) {
            fileUploadPath = fileUploadPath + PATH_SEPARATOR;
        }

        this.baseUrl = baseUrl;
        this.fileUploadPath = fileUploadPath;
        this.bucketName = config.getBucketName();

        // 创建 OSS 客户端
        ossClient = new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());

        // 检查桶是否存在
        if (!ossClient.doesBucketExist(bucketName)) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.bucketNotExist", bucketName));
        }
    }

    /**
     * 文件上传
     *
     * @param file 文件列表
     * @return 上传的文件列表
     */
    @Override
    public FileInfo uploadFile(MultipartFile file) {
        try {
            FileInfo fileInfo = parseMultiFile(file);
            String filepath = fileUploadPath + fileInfo.getFileRelativePath();
            fileInfo.setFileAbsolutePath(bucketName.concat(BUCKET_PATH_SPLIT_LABEL).concat(filepath));
            fileInfo.setDownloadUrl(baseUrl.concat(filepath));
            try (InputStream is = file.getInputStream()) {
                ossClient.putObject(bucketName, filepath, is);
            }
            return fileInfo;
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.uploadFailed"), e);
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
            String bucketName = this.bucketName;
            if (filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
                String[] ary = filepath.split(BUCKET_PATH_SPLIT_LABEL);
                bucketName = ary[0];
                filepath = ary[1];
            }

            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
            OSSObject ossObject = ossClient.getObject(bucketName, filepath);

            // 写入 response
            try (InputStream is = ossObject.getObjectContent();
                 ServletOutputStream os = response.getOutputStream()) {
                int len;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            throw new FileException(ExpMessageCode.of("valid.file.bas.downloadFailed"), e);
        }
    }

    /**
     * 删除文件业务实现
     *
     * @param filepath 文件路径
     */
    @Override
    protected void deleteFileImpl(String filepath) {
        String bucketName = this.bucketName;
        if (filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
            String[] ary = filepath.split(BUCKET_PATH_SPLIT_LABEL);
            bucketName = ary[0];
            filepath = ary[1];
        }

        // 如果文件存在则删除
        if (ossClient.doesObjectExist(bucketName, filepath)) {
            ossClient.deleteObject(bucketName, filepath);
        }
    }

    /**
     * 获取文件上传类型
     *
     * @return 上传类型
     */
    @Override
    public int getFileUploadType() {
        return FileHandlerEnum.ALIYUN.getType();
    }
}
