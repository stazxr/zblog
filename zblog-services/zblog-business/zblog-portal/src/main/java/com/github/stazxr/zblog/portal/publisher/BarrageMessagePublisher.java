package com.github.stazxr.zblog.portal.publisher;

import com.github.stazxr.zblog.content.ext.domain.vo.BarrageMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 弹幕消息推送
 *
 * @author SunTao
 * @since 2027-07-11
 */
@Component
public class BarrageMessagePublisher {
    private static final Logger log = LoggerFactory.getLogger(BarrageMessagePublisher.class);

    private final SimpMessagingTemplate template;

    public BarrageMessagePublisher(SimpMessagingTemplate template){
        this.template=template;
    }

    public void send(BarrageMessageVo barrageMessage) {
        try {
            template.convertAndSend("/topic/barrageMessage", barrageMessage);
        } catch (Exception e) {
            log.error("topic '/topic/barrage' send failed", e);
        }
    }
}
