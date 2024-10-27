package com.github.stazxr.zblog.bas.log.slf4j;

import com.github.stazxr.zblog.bas.log.contants.LogConstants;
import com.github.stazxr.zblog.util.StringUtils;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

/**
 * Custom Log4j filter that checks for the presence of a specific MDC (Mapped Diagnostic Context) key
 * and filters log events accordingly.
 *
 * <p>The filter checks if the MDC contains the key defined in {@link LogConstants#LOG_MDC_PATH_KEY}.
 * If the key is present and its associated value is not blank, the log event passes through the filter.
 * Otherwise, it is filtered out based on the configuration.</p>
 *
 * <p>The filter's behavior (enabled/disabled state and actions on match/mismatch) can be configured
 * using attributes in the Log4j 2 configuration.</p>
 *
 * @author SunTao
 * @since 2024-05-19
 */
@Plugin(name = "LogMdcFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class LogMdcFilter extends AbstractFilter {
    private final boolean enabled;

    /**
     * Constructs a new LogMdcFilter.
     *
     * @param enabled   Indicates if the filter is enabled (true) or disabled (false).
     * @param onMatch   Action to take when the filter matches (MDC key and value are present).
     * @param onMismatch Action to take when the filter does not match (MDC key or value are absent or blank).
     */
    protected LogMdcFilter(boolean enabled, Result onMatch, Result onMismatch) {
        super(onMatch, onMismatch);
        this.enabled = enabled;
    }

    /**
     * Filters the log event based on the presence and value of the MDC key.
     *
     * @param event The log event to filter.
     * @return {@link Result#NEUTRAL}, {@link Result#ACCEPT}, or {@link Result#DENY} based on the filter's decision.
     */
    @Override
    public Result filter(LogEvent event) {
        ReadOnlyStringMap contextData = event.getContextData();
        if (contextData != null && contextData.containsKey(LogConstants.LOG_MDC_PATH_KEY)) {
            String value = contextData.getValue(LogConstants.LOG_MDC_PATH_KEY);
            return filter(StringUtils.isNotBlank(value));
        } else {
            return onMismatch;
        }
    }

    /**
     * Private helper method that applies the filter based on the enabled flag and the boolean flag parameter.
     *
     * @param flag Boolean flag indicating whether the MDC value is considered present and non-blank.
     * @return {@link Result#ACCEPT} if the filter matches and is enabled, otherwise {@link Result#DENY}.
     */
    private Result filter(boolean flag) {
        return enabled ? (flag ? onMatch : onMismatch) : onMismatch;
    }

    /**
     * Factory method for creating an instance of LogMdcFilter based on configuration attributes.
     *
     * @param enabled    Boolean attribute indicating if the filter is enabled.
     * @param onMatch    Result to return when the filter matches.
     * @param onMismatch Result to return when the filter does not match.
     * @return A new instance of LogMdcFilter configured with the provided attributes.
     */
    @PluginFactory
    public static LogMdcFilter createFilter(@PluginAttribute("enabled") Boolean enabled,
                                            @PluginAttribute("onMatch") Result onMatch,
                                            @PluginAttribute("onMismatch") Result onMismatch) {
        // Default values for onMatch and onMismatch are provided by AbstractFilter constructor
        return new LogMdcFilter(enabled != null && enabled, onMatch, onMismatch);
    }
}
