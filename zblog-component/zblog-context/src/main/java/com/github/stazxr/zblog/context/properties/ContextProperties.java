package com.github.stazxr.zblog.context.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.stazxr.zblog.context.entity.ContextTag;
import com.github.stazxr.zblog.context.exception.ContextErrorCode;
import com.github.stazxr.zblog.context.exception.ContextException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * context properties.
 *
 * @since 2024-05-05
 * @author SunTao
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = ContextProperties.CONTEXT_PREFIX)
public class ContextProperties implements Serializable, InitializingBean {
    private static final long serialVersionUID = 1761392456056935111L;

    static final String CONTEXT_PREFIX = "zblog.base.context";

    private static final String DEFAULT_MUSES_TAGS_FILE = "/default-tags.yml";

    /**
     * List of context tags.
     */
    private List<ContextTag> tags;

    public void setTags(List<ContextTag> tags) {
        this.tags = tags;
    }

    public List<ContextTag> getTags() {
        return tags;
    }

    public List<String> getTagNames() {
        return this.tags.stream().map(ContextTag::getTagName).collect(Collectors.toList());
    }

    /**
     * Initializes the bean after properties are set.
     */
    @Override
    public void afterPropertiesSet() {
        try (InputStream is = this.getClass().getResourceAsStream(DEFAULT_MUSES_TAGS_FILE)) {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            ContextProperties defaultContextProperties = objectMapper.readValue(is, ContextProperties.class);
            log.info("Loading default context tags: " + defaultContextProperties.getTags());
            addTags(defaultContextProperties.getTags());
        } catch (Exception e) {
            throw new ContextException(ContextErrorCode.ZCXT001, e);
        } finally {
            log.info("Loaded all context tags: " + tags);
        }
    }

    /**
     * Adds context tags, merging with existing tags.
     *
     * @param tags The list of tags to add.
     */
    private void addTags(List<ContextTag> tags) {
        if (this.tags == null) {
            this.tags = new ArrayList<>(tags);
        } else {
            List<ContextTag> mergeTags = new ArrayList<>(tags);
            for (ContextTag customTag : this.tags) {
                if (mergeTags.contains(customTag)) {
                    // Default tags cannot be customized
                    log.warn("Tag '{}' cannot be customized", customTag.getTagName());
                } else {
                    mergeTags.add(customTag);
                }
            }
            // Assign the merged tags back to 'tags'
            this.tags = new ArrayList<>(mergeTags);
        }
    }
}
