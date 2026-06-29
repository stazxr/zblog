package com.github.stazxr.zblog.audit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 内容审核配置。
 *
 * @author SunTao
 * @since 2026-06-29
 */
@ConfigurationProperties(prefix = "zblog.audit")
public class AuditProperties {
    /**
     * 是否启用审核模块
     */
    private boolean enabled = true;

    /**
     * 是否打印审核日志
     */
    private boolean logEnabled = true;

    /**
     * XSS审核配置
     */
    private Xss xss = new Xss();

    /**
     * 敏感词审核配置
     */
    private Sensitive sensitive = new Sensitive();

    /**
     * 内容规则审核配置
     */
    private Rule rule = new Rule();

    /**
     * 云审核配置
     */
    private Cloud cloud = new Cloud();

    /**
     * 人工审核配置
     */
    private Manual manual = new Manual();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public Xss getXss() {
        return xss;
    }

    public void setXss(Xss xss) {
        this.xss = xss;
    }

    public Sensitive getSensitive() {
        return sensitive;
    }

    public void setSensitive(Sensitive sensitive) {
        this.sensitive = sensitive;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public Manual getManual() {
        return manual;
    }

    public void setManual(Manual manual) {
        this.manual = manual;
    }

    /**
     * XSS审核配置。
     */
    public static class Xss {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 是否支持清洗
         */
        private boolean replace = true;

        /**
         * 是否快速失败
         */
        private boolean reject = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isReplace() {
            return replace;
        }

        public void setReplace(boolean replace) {
            this.replace = replace;
        }

        public boolean isReject() {
            return reject;
        }

        public void setReject(boolean reject) {
            this.reject = reject;
        }
    }

    /**
     * 敏感词审核配置。
     */
    public static class Sensitive {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 是否替换敏感词
         */
        private boolean replace = true;

        /**
         * 是否进入人工审核
         */
        private boolean pending = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isReplace() {
            return replace;
        }

        public void setReplace(boolean replace) {
            this.replace = replace;
        }

        public boolean isPending() {
            return pending;
        }

        public void setPending(boolean pending) {
            this.pending = pending;
        }
    }

    /**
     * 内容规则配置
     */
    public static class Rule {
        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 最大内容长度
         */
        private int maxLength = 500;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(int maxLength) {
            this.maxLength = maxLength;
        }
    }

    /**
     * 云审核配置。
     */
    public static class Cloud {
        /**
         * 是否启用
         */
        private boolean enabled = false;

        /**
         * 超时时间（毫秒）
         */
        private int timeout = 3000;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }

    /**
     * 人工审核配置。
     */
    public static class Manual {
        /**
         * 是否启用
         */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
