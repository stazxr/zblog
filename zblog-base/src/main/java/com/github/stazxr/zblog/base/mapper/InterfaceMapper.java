package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.Interface;
import com.github.stazxr.zblog.base.domain.vo.InterfaceVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口数据持久层
 *
 * @author SunTao
 * @since 2022-06-23
 */
public interface InterfaceMapper extends BaseMapper<Interface> {
    /**
     * 移除所有的接口信息
     */
    void clearInterface();

    /**
     * 根据请求信息查询接口信息
     * @param requestUri    请求地址
     * @param requestMethod 请求方式
     * @return Interface
     */
    Interface selectOneByRequest(@Param("uri") String requestUri, @Param("method") String requestMethod);

    /**
     * 查询权限对应的接口列表
     *
     * @param permId 权限序列
     * @return interfaceList
     */
    List<InterfaceVo> selectPermInterfaces(@Param("permId") Long permId);

    /**
     * 查询权限编码对应的接口列表
     *
     * @param code 权限编码
     * @return interfaceList
     */
    List<InterfaceVo> selectInterfacesByCode(@Param("code") String code);
}
