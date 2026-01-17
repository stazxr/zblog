package com.github.stazxr.zblog.bas.notify.mail.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 邮箱发收人配置
 *
 * @author SunTao
 * @since 2026-01-16
 */
@ConfigurationProperties(prefix= MailFromProperties.MAIL_FROM_PREFIX)
public class MailFromProperties {
    static final String MAIL_FROM_PREFIX= "zblog.base.mail.from";

    /**
     * 发件地址，务必与配置的邮件发送人保持一致
     */
    private String address;

    /**
     * 发件人
     */
    private String name = "Z-BLOG";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
