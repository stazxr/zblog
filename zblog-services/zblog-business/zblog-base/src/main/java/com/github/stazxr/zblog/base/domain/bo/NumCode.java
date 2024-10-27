package com.github.stazxr.zblog.base.domain.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Captcha验证码
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Getter
@Setter
public class NumCode implements Serializable {
    private static final long serialVersionUID = -3337573146305759381L;

    /**
     * 验证码有效时间为5分钟
     */
    private static final int VALID_TIME = 300;

    private String numCode;

    private LocalDateTime expireTime;

    public NumCode(String numCode) {
        this.numCode = numCode;
        this.expireTime = LocalDateTime.now().plusSeconds(VALID_TIME);
    }

    public NumCode(String numCode, int validTime) {
        int validTimeTmp = validTime;
        if (validTimeTmp < 0) {
            // 如果超市时间小于0，则使用默认的超时时间
            validTimeTmp = VALID_TIME;
        }
        this.numCode = numCode;
        this.expireTime = LocalDateTime.now().plusSeconds(validTimeTmp);
    }

    /**
     * 判断验证码是否过期
     *
     * @return boolean
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    @Override
    public String toString() {
        return "NumCode{" + "numCode='" + numCode + '\''
            + ", expireTime=" + expireTime
            + '}';
    }
}
