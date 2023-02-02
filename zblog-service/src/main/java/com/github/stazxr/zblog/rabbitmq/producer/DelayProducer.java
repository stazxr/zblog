package com.github.stazxr.zblog.rabbitmq.producer;

import com.github.stazxr.zblog.rabbitmq.RabbitDelayedConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 延迟消息生产者 - 文章定时发布
 *
 * @author SunTao
 * @since 2022-12-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayProducer {
    private final RabbitTemplate rabbitTemplate;

    /**
     * 生产消息，文章定时发布
     *
     * @param publishId 发布序列
     * @param delay 延时时间，单位毫秒
     */
    public void producerArticlePublishMessage(Long publishId, Integer delay) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelay(delay);
        Message message = new Message(String.valueOf(publishId).getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(RabbitDelayedConfig.EXCHANGE_NAME_DELAYED, RabbitDelayedConfig.ROUTING_KEY_NAME_DELAYED, message);
        log.info("send a delay message[producerArticlePublishMessage]: {publishId: {}, delay: {}}", publishId, delay);
    }
}
