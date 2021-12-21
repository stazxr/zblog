package com.github.stazxr.zblog.core.base;

import java.util.List;

/**
 * BaseMapper
 *
 * @param <E> entity 实体入库
 * @param <D> dto 入参
 * @param <V> vo 返回给页面
 * @author SunTao
 * @since 2021-07-03
 */
public interface BaseMapper<E, D, V> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<E> {
    E dtoToEntity(D dto);

    List<E> dtoToEntity(List<D> dto);

    V entityToVo(E entity);

    List<V> entityToVo(List<E> entity);
}
