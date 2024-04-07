package com.github.stazxr.easycloud.id;

/**
 * 序号生成器接口感知器
 *
 * @author SunTao
 * @since 2024-04-02
 */
public interface IdGeneratorAware {
    /**
     * 设置ID生成器
     *
     * @param idGenerator IdGenerator的实现类
     */
    void setIdGenerator(IdGenerator idGenerator);
}
