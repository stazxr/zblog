package com.github.stazxr.zblog.bas.context;

import com.github.stazxr.zblog.bas.context.entity.ContextTag;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Utility class for interacting with CoreContext.
 * Provides static methods to get and put context tags.
 *
 * @author SunTao
 * @since 2024-07-02
 */
@Slf4j
public class Context {
    /**
     * Retrieves the value associated with the specified key from the context.
     *
     * @param key The key to retrieve from the context
     * @return The value associated with the key, or null if not found
     */
    public static String get(String key) {
        return getContext().get(key);
    }

    /**
     * Adds a single context tag to the context.
     *
     * @param tag The context tag to add
     */
    public static void put(ContextTag tag) {
        getContext().put(tag);
    }

    /**
     * Adds multiple context tags to the context.
     *
     * @param tags The list of context tags to add
     */
    public static void putAll(List<ContextTag> tags) {
        getContext().putAll(tags);
    }

    /**
     * Print context map.
     */
    public static void print() {
        String contextMap = getContext().getContextMapAsString();
        log.debug("Context map: " + contextMap);
    }

    /**
     * Retrieves the current CoreContext instance from the thread-local context holder.
     *
     * @return The current CoreContext instance
     */
    private static CoreContext getContext() {
        return CoreContextHolder.get();
    }
}
