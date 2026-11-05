package com.github.stazxr.zblog.content.autoconfigure.properties;

import com.github.stazxr.zblog.content.domain.enums.ArticleUploadType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 文章上传配置
 *
 * @author SunTao
 * @since 2026-11-06
 */
@ConfigurationProperties(prefix = "zblog.article.upload")
public class ArticleUploadProperties {
    /**
     * 附件允许上传的 Content-Type
     */
    private Set<String> attachment = new HashSet<>();

    /**
     * 视频允许上传的 Content-Type
     */
    private Set<String> video = new HashSet<>();

    /**
     * 图片允许上传的 Content-Type
     */
    private Set<String> image = new HashSet<>();

    public Set<String> getAttachment() {
        return attachment;
    }

    public void setAttachment(Set<String> attachment) {
        this.attachment = attachment;
    }

    public Set<String> getVideo() {
        return video;
    }

    public void setVideo(Set<String> video) {
        this.video = video;
    }

    public Set<String> getImage() {
        return image;
    }

    public void setImage(Set<String> image) {
        this.image = image;
    }

    /**
     * 根据上传类型获取允许上传的 Content-Type
     *
     * @param type 上传类型
     * @return 允许上传的 Content-Type
     */
    public Set<String> getAllowedContentTypes(String type) {
        if (ArticleUploadType.ATTACHMENT.getCode().equalsIgnoreCase(type)) {
            return attachment;
        }
        if (ArticleUploadType.VIDEO.getCode().equalsIgnoreCase(type)) {
            return video;
        }
        if (ArticleUploadType.IMAGE.getCode().equalsIgnoreCase(type)) {
            return image;
        }
        return Collections.emptySet();
    }
}
