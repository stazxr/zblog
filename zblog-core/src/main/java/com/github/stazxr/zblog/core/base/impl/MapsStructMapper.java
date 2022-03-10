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
    /**
     * dto to po
     *
     * @param dto dto
     * @return po
     */
    E dtoToEntity(D dto);

    /**
     * dto list to po list
     *
     * @param dtoList dto list
     * @return po list
     */
    List<E> dtoToEntity(List<D> dtoList);

    /**
     * po to vo
     *
     * @param entity po
     * @return vo
     */
    V entityToVo(E entity);

    /**
     * po list to vo list
     *
     * @param entity po list
     * @return vo list
     */
    List<V> entityToVo(List<E> entity);
}
