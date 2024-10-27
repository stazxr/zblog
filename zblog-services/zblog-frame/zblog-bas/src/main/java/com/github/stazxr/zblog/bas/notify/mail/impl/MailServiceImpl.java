package com.github.stazxr.zblog.bas.notify.mail.impl;

import com.github.stazxr.zblog.bas.notify.mail.MailException;
import com.github.stazxr.zblog.bas.notify.mail.MailReceiver;
import com.github.stazxr.zblog.bas.notify.mail.MailService;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
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
     * @param subject 主体
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(MailReceiver receive, String subject, String content) {
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
            throw new MailException("ZNTFM01", e);
        }
    }

    /**
     * 发送普通HTML邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(MailReceiver receive, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            setReceive(receive, helper);
            helper.setSubject(subject);
            helper.setText(content, true);
            javaMailSender.send(message);
            log.info("send html email success");
        } catch (Exception e) {
            throw new MailException("ZNTFM02", e);
        }
    }

    /**
     * 发送带图片的Html邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     * @param images  图片信息（可以是多个图片，Key为CID，value为图片路径）
     */
    @Override
    public void sendHtmlMailWithImg(MailReceiver receive, String subject, String content, List<Map<String, String>> images) {
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
                Resource resource = getResourceFromPath(path);
                helper.addInline(cid, resource);
            }

            javaMailSender.send(message);
            log.info("send image email success");
        } catch (Exception e) {
            throw new MailException("ZNTFM03", e);
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param receive 收件人
     * @param subject 主体
     * @param content 内容
     * @param files   文件路径
     */
    @Override
    public void sendAttachmentMail(MailReceiver receive, String subject, String content, List<String> files) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            setReceive(receive, helper);
            helper.setSubject(subject);
            helper.setText(content, true);

            // 设置附件信息
            for (String file : files) {
                Resource resource = getResourceFromPath(file);
                String fileName = resource.getFilename();
                if (fileName != null) {
                    helper.addAttachment(fileName, resource);
                } else {
                    throw new IllegalArgumentException("File is invalid: " + file);
                }
            }
            javaMailSender.send(message);
            log.info("send attachment email success");
        } catch (Exception e) {
            throw new MailException("ZNTFM04", e);
        }
    }

    /**
     * 设置邮件接收方和发送方
     *
     * @param receive 接收方
     * @param helper  MimeMessageHelper
     * @throws MessagingException e
     */
    private void setReceive(MailReceiver receive, MimeMessageHelper helper) throws MessagingException {
        helper.setFrom(from);
        helper.setTo(InternetAddress.parse(receive.to()));
        if (StringUtils.isNotBlank(receive.cc())) {
            helper.setCc(InternetAddress.parse(receive.cc()));
        }
        if (StringUtils.isNotBlank(receive.bcc())) {
            helper.setBcc(InternetAddress.parse(receive.bcc()));
        }
    }

    private Resource getResourceFromPath(String path) throws MalformedURLException {
        Resource resource;
        final String http = "http:";
        final String https = "https:";
        final String resourcePathPrefix = "/";
        if (path.startsWith(http) || path.startsWith(https)) {
            resource = new UrlResource(path);
        } else {
            if (!path.startsWith(resourcePathPrefix)) {
                path = resourcePathPrefix.concat(path);
            }
            resource = new FileSystemResource(path);
        }

        if (!resource.exists()) {
            throw new IllegalArgumentException("File not exist: " + path);
        }

        return resource;
    }
}
