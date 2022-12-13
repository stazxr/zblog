package com.github.stazxr.zblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.domain.dto.TalkDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.domain.entity.Talk;
import com.github.stazxr.zblog.domain.vo.TalkVo;

/**
 * 说说服务层
 *
 * @author SunTao
 * @since 2022-12-12
 */
public interface TalkService extends IService<Talk> {
    /**
     * 分页查询说说列表
     *
     * @param queryDto 查询参数
     * @return TalkVoList
     */
    PageInfo<TalkVo> queryTalkListByPage(TalkQueryDto queryDto);

    /**
     * 查询说说详情
     *
     * @param talkId 说说ID
     * @return TalkVo
     */
    TalkVo queryTalkDetail(Long talkId);

    /**
     * 新增或编辑说说
     *
     * @param talkDto 说说信息
     */
    void addOrEditTalk(TalkDto talkDto);

    /**
     * 删除说说
     *
     * @param talkId 说说ID
     */
    void deleteTalk(Long talkId);
}
