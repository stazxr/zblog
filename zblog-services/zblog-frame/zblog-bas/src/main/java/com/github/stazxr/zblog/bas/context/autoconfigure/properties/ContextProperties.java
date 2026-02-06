package com.github.stazxr.zblog.bas.context.autoconfigure.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import com.github.stazxr.zblog.bas.context.exception.ContextErrorCode;
import com.github.stazxr.zblog.bas.context.exception.ContextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration properties for ZBLOG context tags.
 *
 * <p>
 * Loads default tags from a YAML file and merges with user-defined tags.
 * Default tags cannot be overridden by custom tags.
 * </p>
 *
 * <p>
 * Provides lifecycle initialization via {@link InitializingBean}.
 * </p>
 *
 * @author SunTao
 * @since 2024-05-05
 */
@ConfigurationProperties(prefix = ContextProperties.CONFIG_PREFIX)
public class ContextProperties implements Serializable, InitializingBean {
    private static final long serialVersionUID = 1761392456056935111L;

    private static final Logger log = LoggerFactory.getLogger(ContextProperties.class);

    /** Configuration prefix */
    public static final String CONFIG_PREFIX = "zblog.base.context";

    /** Default YAML file containing framework default tags */
    private static final String DEFAULT_MUSES_TAGS_FILE = "/default-tags.yml";

    /** List of all context tags */
    private List<ContextTag> tags = new ArrayList<>();

    public List<ContextTag> getTags() {
        return tags;
    }

    public void setTags(List<ContextTag> tags) {
        this.tags = tags;
    }

    /**
     * Get only tag names.
     *
     * @return List of tag names
     */
    public List<String> getTagNames() {
        return this.tags.stream().map(ContextTag::getTagName).collect(Collectors.toList());
    }

    /**
     * Initializes the bean after properties are set.
     *
     * <p>
     * Loads default tags from YAML and merges them with custom tags.
     * Default tags cannot be overridden.
     * </p>
     *
     * @throws ContextException if YAML loading fails
     */
    @Override
    public void afterPropertiesSet() {
        try (InputStream is = this.getClass().getResourceAsStream(DEFAULT_MUSES_TAGS_FILE)) {
            if (is == null) {
                log.warn("Default tags file {} not found, skipping default tags load.", DEFAULT_MUSES_TAGS_FILE);
                if (this.tags == null) {
                    this.tags = new ArrayList<>();
                }
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            ContextProperties defaultProps = objectMapper.readValue(is, ContextProperties.class);

            log.info("Loading default context tags: {}", defaultProps.getTagNames());
            addTags(defaultProps.getTags());
        } catch (Exception e) {
            throw new ContextException(ContextErrorCode.ZCXT001, e);
        } finally {
            if (this.tags != null) {
                log.info("Loaded all context tags: {}", this.tags.stream().map(ContextTag::getTagName).collect(Collectors.toList()));
            }
        }
    }

    /**
     * Merge default tags with custom tags.
     *
     * <p>
     * Default tags cannot be overridden.
     * </p>
     *
     * @param defaultTags default tags loaded from YAML
     */
    private void addTags(List<ContextTag> defaultTags) {
        if (this.tags == null) {
            this.tags = new ArrayList<>(defaultTags);
        } else {
            List<ContextTag> merged = new ArrayList<>(defaultTags);
            for (ContextTag customTag : this.tags) {
                if (merged.contains(customTag)) {
                    // Prevent overriding default tags
                    log.warn("Tag '{}' cannot be customized; using default value.", customTag.getTagName());
                } else {
                    merged.add(customTag);
                }
            }
            this.tags = merged;
        }
    }
}
