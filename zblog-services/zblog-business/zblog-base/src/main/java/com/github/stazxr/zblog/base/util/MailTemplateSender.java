package com.github.stazxr.zblog.base.util;

import com.github.stazxr.zblog.bas.notify.mail.MailException;
import com.github.stazxr.zblog.bas.notify.mail.MailReceiver;
import com.github.stazxr.zblog.bas.notify.mail.MailService;
import com.github.stazxr.zblog.base.domain.enums.MailTemplate;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.core.config.properties.WebsiteProperties;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * 模板邮件发送工具类
 *
 * @author SunTao
 * @since 2026-01-16
 */
@Component
@RequiredArgsConstructor
public class MailTemplateSender {
    private final TemplateEngine templateEngine;

    private final MailService mailService;

    private final WebsiteProperties websiteProperties;

    /**
     * 通用模板邮件发送
     */
    public void send(MailReceiver receiver, MailTemplate template, Map<String, Object> variables) {
        try {
            // 公共变量
            Context ctx = new Context();
            ctx.setVariable("year", DateUtils.formatNow("yyyy"));
            ctx.setVariable("websiteName", websiteProperties.getName());
            ctx.setVariable("websiteUrl", websiteProperties.getUrl());
            ctx.setVariable("content", template.getContentTemplate());

            // 业务变量
            if (variables != null && variables.size() > 0) {
                variables.forEach(ctx::setVariable);
            }

            // 发送邮件
            String html = templateEngine.process("mail/layout", ctx);
            mailService.sendHtmlMail(receiver, template.getSubject(), html);
        } catch (MailException e) {
            // 邮件服务器故障
            throw e;
        } catch (Exception e) {
            // 邮件发送失败
            throw new MailException(BaseErrorCode.SCOREA004, e);
        }
    }
}
