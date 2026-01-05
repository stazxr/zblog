package com.github.stazxr.zblog.bas.file.autoconfigure;

import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件存储配置项
 *
 * @author SunTao
 * @since 2024-10-24
 */
@ConfigurationProperties(prefix= FileProperties.FILE_PREFIX)
public class FileProperties {
    static final String FILE_PREFIX= "zblog.base.file";

    /**
     * 文件上传模式：see {@link FileHandlerEnum}
     */
    private int model = 1;

    /**
     * 本地配置信息
     */
    private LocalConfig local = new LocalConfig();

    /**
     * 阿里云配置信息
     */
    private AliYunConfig aliYun = new AliYunConfig();

    /**
     * 七牛云配置信息
     */
    private QiNiuYunConfig qiNiuYun = new QiNiuYunConfig();

    @Getter
    @Setter
    public static class LocalConfig {
        /**
         * TODO 文件访问地址
         */
        private String baseUrl;

        /**
         * TODO 文件上传路径
         */
        private String fileUploadPath;
    }

    @Getter
    @Setter
    public static class AliYunConfig {

    }

    @Getter
    @Setter
    public static class QiNiuYunConfig {

    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public LocalConfig getLocal() {
        return local;
    }

    public void setLocal(LocalConfig local) {
        this.local = local;
    }

    public AliYunConfig getAliYun() {
        return aliYun;
    }

    public void setAliYun(AliYunConfig aliYun) {
        this.aliYun = aliYun;
    }

    public QiNiuYunConfig getQiNiuYun() {
        return qiNiuYun;
    }

    public void setQiNiuYun(QiNiuYunConfig qiNiuYun) {
        this.qiNiuYun = qiNiuYun;
    }
}
