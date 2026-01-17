package com.github.stazxr.zblog;

import com.github.stazxr.zblog.bas.notify.mail.MailReceiver;
import com.github.stazxr.zblog.bas.notify.mail.MailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送测试
 *
 * @author SunTao
 * @since 2024-08-04
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "jasypt.encryptor.password=stazxr",
})
@Disabled
@Deprecated
public class MailServiceTest {
    @Autowired
    private MailService mailService;

    @Value("${zblog.email.subject}")
    private String subject;

    @Test
    public void testSendSimpleMail() {
        MailReceiver receiver = MailReceiver.setReceive("1027353579@qq.com", null, null);
        mailService.sendSimpleMail(receiver, subject, "你好");
    }

    @Test
    public void testSendHtmlMail() {
        MailReceiver receiver = MailReceiver.setReceive("1027353579@qq.com", null, null);
        mailService.sendHtmlMail(receiver, subject, "<h1>你好</h1><br /><p>测试</p>");
    }

    @Test
    public void testResource() throws IOException {
        Resource resource = new UrlResource("https://suntaoblog.oss-cn-beijing.aliyuncs.com/upload/2023-02/17/3565224843357978624.jpg");
        System.out.println(resource.getURL());
        System.out.println(resource.exists());
        System.out.println(resource.isFile());
    }

    @Test
    public void testSendHtmlMailWithImg() {
        MailReceiver receiver = MailReceiver.setReceive("1027353579@qq.com", null, null);
        List<Map<String, String>> images = new ArrayList<>();
        Map<String, String> image1 = new HashMap<>();
        image1.put("cid", "image1");
        image1.put("path", "https://suntaoblog.oss-cn-beijing.aliyuncs.com/upload/2023-02/17/3565224843357978624.jpg");
        Map<String, String> image2 = new HashMap<>();
        image2.put("cid", "image2");
        image2.put("path", "https://suntaoblog.oss-cn-beijing.aliyuncs.com/upload/2023-02/17/3565489240265457664.jpg");
        images.add(image1);
        images.add(image2);
        mailService.sendHtmlMailWithImg(receiver, subject, "<h1>你好</h1><br /><img src='cid:image1' /><br /><p>测试</p><img src='cid:image2' />", images);
    }

    @Test
    public void testSendAttachmentMail() {
        MailReceiver receiver = MailReceiver.setReceive("1027353579@qq.com", null, null);
        List<String> files = new ArrayList<>();
        files.add("https://suntaoblog.oss-cn-beijing.aliyuncs.com/upload/2023-02/17/3565224843357978624.jpg");
        mailService.sendAttachmentMail(receiver, subject, "<h1>你好</h1><br /><img src='cid:image1' /><br /><p>测试</p>", files);
    }
}
