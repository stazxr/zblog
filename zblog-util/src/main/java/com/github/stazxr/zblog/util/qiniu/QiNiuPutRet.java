package com.github.stazxr.zblog.util.qiniu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 七牛云上传响应对下封装
 *
 * @author SunTao
 * @since 2022-10-31
 */
@Getter
@Setter
@ToString
public class QiNiuPutRet {
    private String key;

    private String hash;

    private String bucket;

    private long fsize;
}