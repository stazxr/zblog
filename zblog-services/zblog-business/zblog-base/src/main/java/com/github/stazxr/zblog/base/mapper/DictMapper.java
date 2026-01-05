package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.bo.NameValue;
import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
import com.github.stazxr.zblog.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典管理数据层
 *
 * @author SunTao
 * @since 2021-02-20
 */
public interface DictMapper extends BaseMapper<Dict> {
    /**
     * 分页查询字典列表
     *
     * @param queryDto 查询参数
     * @return dictList
     */
    List<DictVo> selectDictList(DictQueryDto queryDto);

    /**
     * 查询字典子列表
     *
     * @param pid PID
     * @return dictList
     */
    List<DictVo> selectChildList(@Param("pid") Long pid);

    /**
     * 查询字典信息
     *
     * @param dictId 字典序列
     * @return DictVo
     */
    DictVo selectDictDetail(@Param("dictId") Long dictId);

    /**
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return List<NameValue>
     */
    List<NameValue> selectNameValuesByDictKey(@Param("dictKey") String dictKey);

    /**
     * 根据字典KEY查询配置信息
     *
     * @param dictKey 字典KEY
     * @return VALUE
     */
    String selectDictValueByDictKey(@Param("dictKey") String dictKey);

    /**
     * 根据字典KEY查询配置信息列表
     *
     * @param dictKey 字典KEY
     * @return VALUES
     */
    List<String> selectDictValuesByDictKey(@Param("dictKey") String dictKey);
}
