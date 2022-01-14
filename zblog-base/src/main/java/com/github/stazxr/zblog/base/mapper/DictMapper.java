package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据持久层
 *
 * @author SunTao
 * @since 2021-02-20
 */
public interface DictMapper extends BaseMapper<Dict> {

    List<Dict> selectItems(@Param("dictKey") String key);
}
