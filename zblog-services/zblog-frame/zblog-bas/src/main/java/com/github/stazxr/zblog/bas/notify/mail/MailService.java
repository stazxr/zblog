package com.github.stazxr.zblog.bas.notify.mail;

import java.util.List;
import java.util.Map;

/**
 * JavaEmail发送邮件接口API
 *
 * @author SunTao
 * @since 2020-11-14
 */
public interface MailService {
    /**
     * 发送普通文本邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     */
    void sendSimpleMail(MailReceiver receive, String subject, String content);

    /**
     * 发送普通HTML邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     */
    void sendHtmlMail(MailReceiver receive, String subject, String content);

    /**
     * 发送带图片的Html邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     * @param images  图片信息（可以是多个图片，Key为CID，value为图片路径）
     */
    void sendHtmlMailWithImg(MailReceiver receive, String subject, String content, List<Map<String, String>> images);

    /**
     * 发送带附件的邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     * @param files   文件路径
     */
    void sendAttachmentMail(MailReceiver receive, String subject, String content, List<String> files);
}
