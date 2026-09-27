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

    /**
     * 发件人站点配置
     */
    private Website website = new Website();

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

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public static class Website {
        /**
         * 站点名称
         */
        private String name = "Z-BLOG";

        /**
         * 站点地址
         */
        private String url = "http://localhost:31945";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
