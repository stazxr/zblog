package com.github.stazxr.zblog.base.domain.enums;

/**
 * 邮箱发送模版
 *
 * @author SunTao
 * @since 2026-01-16
 */
public enum MailTemplate {
    EMAIL_CODE("mail/code", "邮箱验证码");

    private final String contentTemplate;

    private final String subject;

    MailTemplate(String contentTemplate, String subject) {
        this.contentTemplate = contentTemplate;
        this.subject = subject;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public String getSubject() {
        return subject;
    }
}
