package com.github.stazxr.zblog.rabbitmq.consumer;

import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.domain.enums.ArticleStatus;
import com.github.stazxr.zblog.domain.vo.ArticleVo;
import com.github.stazxr.zblog.mapper.ArticleMapper;
import com.github.stazxr.zblog.rabbitmq.RabbitDelayedConfig;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 延迟消息消费者
 *
 * @author SunTao
 * @since 2022-12-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DelayConsumer {
    private final ArticleMapper articleMapper;

    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(queues = {RabbitDelayedConfig.QUEUE_NAME_DELAYED})
    public void delayQueue(Message message) {
        ArticleVo article = null;
        try {
            String publishId = new String(message.getBody());
            log.info("consumer a delay message: {publishId: {}, time: {}}", publishId, DateUtils.formatNow());

            article = articleMapper.selectArticleByPublishId(publishId);
            Assert.notNull(article, "发布记录不存在【" + publishId + "】");
            Assert.notNull(article.getId() == null, "文章不存在【" + publishId + "】");
            Assert.isTrue(ArticleStatus.PUBLISH_REVIEW.getType().equals(article.getArticleStatus()), "文章未审核，发布失败【" + publishId + "】");
            Assert.isTrue(!ArticleStatus.TIME_PUBLISH.getType().equals(article.getArticleStatus()), "文章状态异常，发布失败【" + publishId + "】");
            articleMapper.updateArticleStatus(article.getId(), ArticleStatus.PUBLISHED.getType());
            articleMapper.updateArticleDesc(article.getId(), "文章于 " + DateUtils.formatNow() + " 定时发布成功");
            log.info("文章【{}】定时发布成功，序列为：{}", article.getTitle(), article.getId());
        } catch (ServiceException e) {
            log.error("消息消费失败", e);

            // 保存发布失败原因
            if (article != null && article.getId() != null) {
                articleMapper.updateArticleDesc(article.getId(), "定时发布失败：" + e.getMessage());
            }
        } catch (Exception e) {
            log.error("消息消费异常", e);

            // 保存发布失败原因
            if (article != null && article.getId() != null) {
                articleMapper.updateArticleDesc(article.getId(), "定时发布失败：消息消费异常");
            }
        }
    }
}
