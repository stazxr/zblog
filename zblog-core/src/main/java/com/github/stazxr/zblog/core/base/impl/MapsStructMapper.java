package com.github.stazxr.zblog.core.base.impl;


import com.github.stazxr.zblog.core.base.BaseMapper;

import java.util.List;

/**
 * BaseMapper
 *
 * @param <E> entity 数据库实体
 * @param <D> dto 入参
 * @param <V> vo 返回给页面
 * @author SunTao
 * @since 2021-07-03
 */
public interface MapsStructMapper<E, D, V> extends BaseMapper<E> {
    E dtoToEntity(D dto);

    List<E> dtoToEntity(List<D> dto);

    V entityToVo(E entity);

    List<V> entityToVo(List<E> entity);
}
