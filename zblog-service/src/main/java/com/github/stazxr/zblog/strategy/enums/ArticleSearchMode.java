package com.github.stazxr.zblog.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索类型枚举
 *
 * @author yezhiqiu（原作者）
 * @date 2021/07/27
 */
@Getter
@AllArgsConstructor
public enum ArticleSearchMode {
    /**
     * MYSQL
     */
    MYSQL("mysql", "mySqlArticleSearchStrategyImpl");

    /**
     * 模式
     */
    private final String mode;

    /**
     * 策略
     */
    private final String strategy;

    /**
     * 获取策略
     *
     * @param mode 模式
     * @return {@link String} 搜索策略
     */
    public static String getStrategy(String mode) {
        for (ArticleSearchMode value : ArticleSearchMode.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }
}
