package com.github.stazxr.zblog.base.mapper;

import com.github.stazxr.zblog.base.domain.dto.query.DictQueryDto;
import com.github.stazxr.zblog.base.domain.entity.Dict;
import com.github.stazxr.zblog.base.domain.vo.DictVo;
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

    /**
     * 修改字典状态
     *
     * @param dictId  字典ID
     * @param enabled 字典状态
     * @return 影响行数
     */
    int updateDictStatus(@Param("dictId") Long dictId, @Param("enabled") Boolean enabled);

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
     * 删除字典
     *
     * @param dictId 字典序列
     * @param type   字典类型
     */
    void deleteDict(@Param("dictId") Long dictId, @Param("type") Integer type);

    /**
     * 根据KEY查询VALUE
     *
     * @param key 字典KEY
     * @return VALUE
     */
    String selectSingleValue(@Param("key") String key);

    /**
     * 根据 KEY 修改字典值
     *
     * @param dictKey   KEY
     * @param dictValue VALUE
     */
    void updateSingleValue(@Param("dictKey") String dictKey, @Param("dictValue") String dictValue);
}
