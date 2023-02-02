package com.github.stazxr.zblog.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于 Rabbit 的文章定时发布的延迟队列配置
 *
 * @author SunTao
 * @since 2022-12-27
 */
@Configuration
public class RabbitDelayedConfig {
    public static final String QUEUE_NAME_DELAYED = "DELAY.QUEUE";

    public static final String EXCHANGE_NAME_DELAYED = "DELAY.EXCHANGE";

    public static final String ROUTING_KEY_NAME_DELAYED = "DELAY.MESSAGE";

    public static final String ROUTING_KEY_DELAYED = "DELAY.#";

    @Bean(QUEUE_NAME_DELAYED)
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME_DELAYED).build();
    }

    @Bean(EXCHANGE_NAME_DELAYED)
    public CustomExchange exchange() {
        // 在这里声明一个主题类型的延迟队列，当然其他类型的也可以。
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "topic");
        return new CustomExchange(EXCHANGE_NAME_DELAYED, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Binding bindingNotify(@Qualifier(QUEUE_NAME_DELAYED) Queue queue, @Qualifier(EXCHANGE_NAME_DELAYED) CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(ROUTING_KEY_DELAYED).noargs();
    }
}
