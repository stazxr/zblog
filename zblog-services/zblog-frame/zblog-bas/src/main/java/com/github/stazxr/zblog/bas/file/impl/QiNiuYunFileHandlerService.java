package com.github.stazxr.zblog.bas.file.impl;

import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileException;
import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class QiniuyunFileHandlerService extends BaseFileService {
    /**
     * 域名
     */
    private final String baseUrl;

    /**
     * 上传路径
     */
    private final String fileUploadPath;

    /**
     * 存储空间名称
     */
    private final String zoneName;



    private final UploadManager uploadManager;
    private final BucketManager bucketManager;
    private final Auth auth;

    public QiniuyunFileHandlerService(FileProperties.QiniuYunConfig config) {
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
        this.zoneName = config.getZoneName();

        Configuration cfg = new Configuration(parseRegion(config.getZone()));
        this.uploadManager = new UploadManager(cfg);

        this.auth = Auth.create(config.getAk(), config.getSk());
        this.bucketManager = new BucketManager(auth, cfg);
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
            fileInfo.setFileAbsolutePath(zoneName.concat(BUCKET_PATH_SPLIT_LABEL).concat(filepath));
            fileInfo.setDownloadUrl(buildAccessUrl(filepath));
            String uploadToken = auth.uploadToken(zoneName);
            uploadManager.put(file.getInputStream(), filepath, uploadToken, null, null);
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
            if (filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
                String[] ary = filepath.split(BUCKET_PATH_SPLIT_LABEL);
                filepath = ary[1];
            }
            String url = buildAccessUrl(filepath);
            String encodedUrl = URLEncoder.encode(url, "UTF-8");
            response.sendRedirect(encodedUrl);
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
        String zoneName = this.zoneName;
        if (filepath.contains(BUCKET_PATH_SPLIT_LABEL)) {
            String[] ary = filepath.split(BUCKET_PATH_SPLIT_LABEL);
            zoneName = ary[0];
            filepath = ary[1];
        }

        try {
            if (bucketManager.stat(zoneName, filepath) != null) {
                bucketManager.delete(zoneName, filepath);
            }
        } catch (QiniuException e) {
            Response r = e.response;
            try {
                throw new FileException("七牛云文件删除失败: " + r.bodyString(), e);
            } catch (QiniuException ignored) {
                throw new FileException("七牛云文件删除失败", e);
            }
        }
    }

    /**
     * 获取文件上传类型
     *
     * @return 上传类型
     */
    @Override
    public int getFileUploadType() {
        return FileHandlerEnum.QINIUYUN.getType();
    }

    private Region parseRegion(String zone) {
        switch (zone) {
            case "huadong":
                return Region.huadong();
            case "huabei":
                return Region.huabei();
            case "huanan":
                return Region.huanan();
            case "beimei":
                return Region.beimei();
            case "xinjiapo":
                return Region.xinjiapo();
            default:
                return Region.autoRegion();
        }
    }

    private String buildAccessUrl(String key) {
        return baseUrl + key;
    }
}
