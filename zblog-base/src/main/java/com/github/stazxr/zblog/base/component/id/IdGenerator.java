package com.github.stazxr.zblog.base.component.id;

/**
 * 全局唯一ID生成器
 *
 * @author SunTao
 * @since 2021-12-12
 */
public interface IdGenerator {
    /**
     * 生成唯一ID
     *
     * @return Long
     */
    Long generateId();
}
