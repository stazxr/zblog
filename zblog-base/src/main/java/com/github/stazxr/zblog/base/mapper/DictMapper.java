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
    /**
     * 根据key查找字典项列表
     *
     * @param key Key
     * @return 字典项列表
     */
    List<Dict> selectItems(@Param("dictKey") String key);
}
