package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.Router;
import com.github.stazxr.zblog.base.domain.vo.RouterVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
}
