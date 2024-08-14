package com.github.stazxr.zblog.context;

import com.github.stazxr.zblog.context.entity.ContextTag;
import com.github.stazxr.zblog.context.exception.ContextErrorCode;
import com.github.stazxr.zblog.context.exception.ContextException;
import com.github.stazxr.zblog.context.properties.ContextProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core context management for framework.
 *
 * This class manages context tags and their values within a ConcurrentHashMap.
 * It provides methods to retrieve and store context tags based on configuration properties.
 *
 * @author SunTao
 * @since 2024-07-02
 */
@Slf4j
class CoreContext {
    private static final Map<String, String> CONTEXT_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Boolean> CACHE_MAP = new ConcurrentHashMap<>();

    private final ContextProperties contextProperties;

    public CoreContext(ContextProperties contextProperties) {
        if (contextProperties == null) {
            throw new ContextException(ContextErrorCode.ZCXT002);
        }

        this.contextProperties = contextProperties;
    }

    /**
     * Retrieves the value associated with the given key from the context map.
     *
     * @param key The key to retrieve the value for
     * @return The value associated with the key, or null if the key is not found
     */
    public String get(String key) {
        return CONTEXT_MAP.get(key);
    }

    /**
     * Stores the value of the given context tag in the context map if it is configured to be cached.
     *
     * @param contextTag The context tag to store
     */
    public void put(ContextTag contextTag) {
        // Check if the tag is configured to be cached
        String tagName = contextTag.getTagName();
        if (CACHE_MAP.containsKey(tagName)) {
            if (CACHE_MAP.get(tagName)) {
                CONTEXT_MAP.put(tagName, contextTag.getTagValue());
            }
        } else {
            // Initialize cache status for the tag
            List<String> tags = contextProperties.getTagNames();
            boolean isAllowedTag = tags.stream().anyMatch(tag -> tag.toLowerCase(Locale.ROOT).equals(tagName.toLowerCase(Locale.ROOT)));
            CACHE_MAP.put(tagName, isAllowedTag);
            if (isAllowedTag) {
                // Store the tag value if it is configured to be cached
                CONTEXT_MAP.put(tagName, contextTag.getTagValue());
            }
        }
    }

    /**
     * Stores the values of all provided context tags in the context map.
     *
     * @param contextTags The list of context tags to store
     */
    public void putAll(List<ContextTag> contextTags) {
        contextTags.forEach(this::put);
    }

    /**
     * get context map by toString().
     *
     * @return CONTEXT_MAP.toString()
     */
    public String getContextMapAsString() {
        return CONTEXT_MAP.toString();
    }
}
