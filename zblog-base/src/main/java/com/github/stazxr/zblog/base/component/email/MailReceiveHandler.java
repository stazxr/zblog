package com.github.stazxr.zblog.base.component.email;

import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.collection.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件接收人处理器
 *
 * @author SunTao
 * @since 2020-11-15
 */
public class MailReceiveHandler {
    /**
     * 邮件接收人，抄送人，密送人信息
     */
    private final Map<String, String> receive = new HashMap<>(CollectionUtils.mapSize(3));

    /**
     * 邮件接收方，多个逗号分割
     */
    private static final String MAIL_TO = "to";

    /**
     * 邮件抄送方，多个逗号分割
     */
    private static final String MAIL_CC = "cc";

    /**
     * 邮件密送方，多个逗号分割
     */
    private static final String MAIL_BCC = "bcc";

    /**
     * 获取接收方
     *
     * @return to
     */
    public String to() {
        return this.receive.get(MAIL_TO);
    }

    /**
     * 获取抄送方
     *
     * @return cc
     */
    public String cc() {
        return this.receive.get(MAIL_CC);
    }

    /**
     * 获取密送方
     *
     * @return bcc
     */
    public String bcc() {
        return this.receive.get(MAIL_BCC);
    }

    /**
     * 设置邮件接收方
     *
     * @param to 邮件接收方
     * @return MailReceiveHandler
     */
    public static MailReceiveHandler setReceive(String to) {
        return setReceive(to, null, null);
    }

    /**
     * 设置邮件接收方
     *
     * @param to 接收方
     * @param cc 抄送方
     * @return MailReceiveHandler
     */
    public static MailReceiveHandler setReceive(String to, String cc) {
        return setReceive(to, cc, null);
    }

    /**
     * 设置邮件接收方
     *
     * @param to  接收方
     * @param cc  抄送方
     * @param bcc 密送方
     * @return MailReceiveHandler
     */
    public static MailReceiveHandler setReceive(String to, String cc, String bcc) {
        Assert.notNull(to, "邮件接收方不能为空");
        MailReceiveHandler handler = new MailReceiveHandler();
        handler.receive.clear();
        handler.receive.put(MAIL_TO, to);
        handler.receive.put(MAIL_CC, cc);
        handler.receive.put(MAIL_BCC, bcc);
        return handler;
    }
}
