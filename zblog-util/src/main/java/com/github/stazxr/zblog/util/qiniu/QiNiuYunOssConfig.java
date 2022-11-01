package com.github.stazxr.zblog.util.qiniu;

import lombok.Getter;
import lombok.Setter;

/**
 * 七牛云对象存储配置信息
 *
 * @author SunTao
 * @since 2022-10-30
 */
@Getter
@Setter
public class QiNiuYunOssConfig {
    /**
     * AK
     */
    private String ak = "";

    /**
     * SK
     */
    private String sk = "";

    /**
     * 存储区域
     */
    private String zone = "";

    /**
     * 存储空间
     */
    private String bucketName = "";

    @Override
    public String toString() {
        return "QiNiuYunOssConfig{" +
                ", ak='" + ak + '\'' +
                ", sk='" + sk + '\'' +
                ", zone='" + zone + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }
}
