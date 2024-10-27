package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.RouterQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.base.domain.vo.RouterVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 路由数据持久层
 *
 * @author SunTao
 * @since 2021-07-10
 */
public interface RouterMapper extends BaseMapper<Router> {
    /**
     * 移除所有的路由信息
     */
    void clearRouter();

    /**
     * 根据路由编码查询
     *
     * @param code 路由编码
     * @return RouterVo
     */
    RouterVo selectRouterVoByCode(@Param("code") String code);

    /**
     * 分页查询路由列表
     *
     * @param queryDto 查询参数
     * @return routerList
     */
    List<RouterVo> selectRouterList(RouterQueryDto queryDto);

    /**
     * 分页查询黑白名单列表
     *
     * @param queryDto 查询参数
     * @return blackOrWhiteList
     */
    List<DictVo> selectBlackOrWhiteList(RouterQueryDto queryDto);

    /**
     * 查询日志展示标识
     *
     * @param uri    请求地址
     * @param method 请求方式
     * @return LogShowFlag
     */
    Boolean selectLogShowFlag(@Param("uri") String uri, @Param("method") String method);

    /**
     * 新增日志展示标识
     *
     * @param uri        请求地址
     * @param method     请求方式
     * @param logShowed  是否展示日志
     * @param createUser 创建用户
     * @param createTime 创建时间
     * @return 影响行数
     */
    int insertLogShowFlag(String uri, String method, boolean logShowed, String createUser, String createTime);

    /**
     * 修改日志展示标识
     *
     * @param uri        请求地址
     * @param method     请求方式
     * @param logShowed  是否展示日志
     * @param updateUser 更新用户
     * @param updateTime 更新时间
     * @return 影响行数
     */
    int updateLogShowFlag(String uri, String method, boolean logShowed, String updateUser, String updateTime);

    /**
     * 查询所有的权限编码
     *
     * @return codes
     */
    Set<String> selectCode();
}
