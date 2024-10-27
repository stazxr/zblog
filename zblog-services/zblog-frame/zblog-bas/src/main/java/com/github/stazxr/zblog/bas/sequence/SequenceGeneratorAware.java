package com.github.stazxr.zblog.bas.sequence;

/**
 * 序号生成器接口感知器
 *
 * @author SunTao
 * @since 2024-04-02
 */
public interface SequenceGeneratorAware {
    /**
     * 设置ID生成器
     *
     * @param idGenerator IdGenerator的实现类
     */
    void setSequenceGenerator(SequenceGenerator idGenerator);
}
