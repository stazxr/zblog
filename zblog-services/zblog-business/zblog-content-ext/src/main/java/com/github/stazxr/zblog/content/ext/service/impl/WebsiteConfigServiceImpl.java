package com.github.stazxr.zblog.content.ext.service.impl;

import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.content.ext.converter.WebsiteConfigConverter;
import com.github.stazxr.zblog.content.ext.domain.dto.CommentEmojiDto;
import com.github.stazxr.zblog.content.ext.domain.dto.WebsiteConfigDto;
import com.github.stazxr.zblog.content.ext.domain.entity.CommentEmoji;
import com.github.stazxr.zblog.content.ext.domain.entity.WebsiteConfig;
import com.github.stazxr.zblog.content.ext.domain.vo.CommentEmojiVo;
import com.github.stazxr.zblog.content.ext.domain.vo.WebsiteConfigVo;
import com.github.stazxr.zblog.content.ext.mapper.CommentEmojiMapper;
import com.github.stazxr.zblog.content.ext.mapper.WebsiteConfigMapper;
import com.github.stazxr.zblog.content.ext.service.WebsiteConfigService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 网站配置业务实现层
 *
 * @author SunTao
 * @since 2026-08-20
 */
@Service
@RequiredArgsConstructor
public class WebsiteConfigServiceImpl implements WebsiteConfigService {
    private final WebsiteConfigMapper websiteConfigMapper;

    private final CommentEmojiMapper commentEmojiMapper;

    private final WebsiteConfigConverter websiteConfigConverter;

    /**
     * 查询网站配置详情
     *
     * @return WebsiteConfigVo
     */
    @Override
    public WebsiteConfigVo queryWebsiteConfigDetail() {
        WebsiteConfig websiteConfig = websiteConfigMapper.selectById(1L);
        WebsiteConfigVo websiteConfigVo = websiteConfigConverter.entityToVo(websiteConfig);
        if (websiteConfigVo != null) {
            List<CommentEmojiVo> emojiVoList = commentEmojiMapper.selectCommentEmojis();
            websiteConfigVo.setCommentEmojis(emojiVoList);
        }
        return ThrowUtils.requireNonNull(websiteConfigVo, BaseErrorCode.ECOREA001);
    }

    /**
     * 编辑网站配置
     *
     * @param websiteConfigDto 网站配置信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editWebsiteConfig(WebsiteConfigDto websiteConfigDto) {
        WebsiteConfig websiteConfig = websiteConfigConverter.dtoToEntity(websiteConfigDto);
        if (websiteConfig.getFooterNavbarSwitch() == null) {
            websiteConfig.setFooterNavbarSwitch(true);
        }
        if (websiteConfig.getFriendLinkApplySwitch() == null) {
            websiteConfig.setFriendLinkApplySwitch(true);
        }
        if (websiteConfig.getHttpsSwitch() == null) {
            websiteConfig.setHttpsSwitch(false);
        }
        if (websiteConfig.getBarrageMessageLoadSize() == null) {
            websiteConfig.setBarrageMessageLoadSize(200); // 默认 200
        }
        int updateRow = websiteConfigMapper.updateById(websiteConfig);
        ThrowUtils.when(updateRow != 1).system(BaseErrorCode.SCOREA002);

        // 更新表情包
        List<CommentEmojiDto> commentEmojis = websiteConfigDto.getCommentEmojis();
        if (commentEmojis.size() > 0) {
            commentEmojiMapper.deleteAll();
            List<CommentEmoji> commentEmojiList = new ArrayList<>();
            for (int i = 0; i < commentEmojis.size(); i++) {
                CommentEmojiDto commentEmoji = commentEmojis.get(i);
                CommentEmoji emoji = new CommentEmoji();
                emoji.setId(SequenceUtils.getId());
                emoji.setName(commentEmoji.getName());
                emoji.setCode("[" + commentEmoji.getName() + "]");
                emoji.setUrl(commentEmoji.getUrl());
                emoji.setSort(i + 1);
                commentEmojiList.add(emoji);
            }
            commentEmojiMapper.insertBatch(commentEmojiList);
        }
    }
}
