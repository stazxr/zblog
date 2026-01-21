package com.github.stazxr.zblog.core.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

/**
 * BaseMapper
 *
 * @param <E> entity 实体入库
 * @author SunTao
 * @since 2021-07-03
 */
public interface BaseMapper<E> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<E> {
    /**
     * 根据 ID 修改
     *
     * @param entity 实体对象
     */
    @Override
    default int updateById(E entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象 (set 条件值,可以为 null)
     * @param updateWrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    @Override
    default int update(E entity, Wrapper<E> updateWrapper) {
        throw new UnsupportedOperationException();
    }
}
