package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.bas.router.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源数据持久层
 *
 * @author SunTao
 * @since 2025-07-06
 */
public interface ResourceMapper {
    /**
     * 根据资源请求地址和资源请求方式查找资源
     *
     * @param requestUri    资源请求地址
     * @param requestMethod 资源请求方式
     * @return Resource
     */
    Resource selectByUriAndMethod(@Param("requestUri") String requestUri, @Param("requestMethod") String requestMethod);

    /**
     * 删除所有资源信息
     */
    void deleteAll();

    /**
     * 批量插入资源列表
     *
     * @param resources 资源列表
     */
    void insertBatch(@Param("resources") List<Resource> resources);
}
