package com.github.stazxr.zblog.base.component.email.impl;

import com.github.stazxr.zblog.base.component.email.MailReceiveHandler;
import com.github.stazxr.zblog.base.component.email.MailService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * JavaEmail发送邮件接口API实现
 *
 * @author SunTao
 * @since 2020-11-14
 */
@Slf4j
@Component
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * 发送普通文本邮件
     *
     * @param receive 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(MailReceiveHandler receive, String subject, String content) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(receive.to());
            if (StringUtils.isNotBlank(receive.cc())) {
                mailMessage.setCc(receive.cc());
            }
            if (StringUtils.isNotBlank(receive.bcc())) {
                mailMessage.setBcc(receive.bcc());
            }

            mailMessage.setSubject(subject);
            mailMessage.setText(content);
            javaMailSender.send(mailMessage);
            log.info("send simple email success");
        } catch (Exception e) {
            log.error("send simple email failed");
            throw new ServiceException(e);
        }
    }

    /**
     * 发送普通HTML邮件
     *
     * @param receive 收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(MailReceiveHandler receive, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            setReceive(receive, helper);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
            log.info("send html email success");
        } catch (MessagingException e) {
            log.error("send html email failed");
            throw new ServiceException(e);
        }
    }

    /**
     * 发送带图片的Html邮件
     *
     * @param receive 收件人
     * @param subject 主题
     * @param content 内容
     * @param images  图片信息（可以是多个图片，Key为CID，value为图片路径）
     */
    @Override
    public void sendHtmlMailWithImg(MailReceiveHandler receive, String subject, String content, List<Map<String, String>> images) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            setReceive(receive, helper);
            helper.setSubject(subject);
            helper.setText(content, true);

            // 设置图片信息
            for (Map<String, String> image : images) {
                String cid = image.get("cid");
                String path = image.get("path");
                FileSystemResource resource = new FileSystemResource(path);
                if (resource.exists() && resource.isFile()) {
                    helper.addInline(cid, resource);
                }
            }

            javaMailSender.send(message);
            log.info("send image email success");
        } catch (MessagingException e) {
            log.error("send image email failed");
            throw new ServiceException(e);
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param receive 收件人
     * @param subject 主题
     * @param content 内容
     * @param files   文件路径
     */
    @Override
    public void sendAttachmentMail(MailReceiveHandler receive, String subject, String content, List<String> files) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            setReceive(receive, helper);
            helper.setSubject(subject);
            helper.setText(content, true);

            // 设置附件信息
            for (String file : files) {
                FileSystemResource resource = new FileSystemResource(new File(file));
                if (resource.exists() && resource.isFile()) {
                    String fileName = resource.getFilename();
                    if (fileName != null) {
                        helper.addAttachment(fileName, resource);
                    }
                }
            }
            javaMailSender.send(message);
            log.info("send attachment email success");
        } catch (Exception e) {
            log.error("send attachment email failed");
            throw new ServiceException(e);
        }
    }

    /**
     * 设置邮件接收方和发送方
     *
     * @param receive 接收方
     * @param helper  MimeMessageHelper
     * @throws MessagingException e
     */
    private void setReceive(MailReceiveHandler receive, MimeMessageHelper helper) throws MessagingException {
        helper.setFrom(from);
        helper.setTo(InternetAddress.parse(receive.to()));
        if (StringUtils.isNotBlank(receive.cc())) {
            helper.setCc(InternetAddress.parse(receive.cc()));
        }
        if (StringUtils.isNotBlank(receive.bcc())) {
            helper.setBcc(InternetAddress.parse(receive.bcc()));
        }
    }
}
