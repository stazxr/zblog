package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.domain.entity.FileRelation;
import com.github.stazxr.zblog.base.mapper.FileRelationMapper;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.converter.TalkConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.domain.dto.TalkDto;
import com.github.stazxr.zblog.domain.dto.query.TalkQueryDto;
import com.github.stazxr.zblog.domain.entity.Talk;
import com.github.stazxr.zblog.domain.enums.TalkStatus;
import com.github.stazxr.zblog.domain.vo.TalkVo;
import com.github.stazxr.zblog.mapper.TalkMapper;
import com.github.stazxr.zblog.service.TalkService;
import com.github.stazxr.zblog.util.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 说说服务实现层
 *
 * @author SunTao
 * @since 2022-12-12
 */
@Service
@RequiredArgsConstructor
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {
    private final TalkConverter talkConverter;

    private final FileRelationMapper fileRelationMapper;

    /**
     * 分页查询说说列表
     *
     * @param queryDto 查询参数
     * @return TalkVoList
     */
    @Override
    public PageInfo<TalkVo> queryTalkListByPage(TalkQueryDto queryDto) {
        // 查询当前用户信息
        String loginUsername = SecurityUtils.getLoginUsername();
        queryDto.setLoginUser(loginUsername);

        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectTalkList(queryDto));
    }

    /**
     * 查询说说详情
     *
     * @param talkId 说说ID
     * @return TalkVo
     */
    @Override
    public TalkVo queryTalkDetail(Long talkId) {
        Assert.notNull(talkId, "参数【talkId】不能为空");
        TalkVo talkVo = baseMapper.selectTalkDetail(talkId);
        Assert.notNull(talkVo, "查询说说信息失败，说说【" + talkId + "】不存在");

        // 查询说说对应的图片文件列表
        List<File> fileList = fileRelationMapper.selectByBusinessId(talkId);
        talkVo.setImagesFileList(fileList);
        return talkVo;
    }

    /**
     * 新增或编辑说说
     *
     * @param talkDto 说说信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrEditTalk(TalkDto talkDto) {
        Talk talk = talkConverter.dtoToEntity(talkDto);
        checkTalk(talk);
        Assert.nonTrue(saveOrUpdate(talk), "操作失败");

        // 保存置顶信息
        if (talk.getIsTop()) {
            baseMapper.updateTopStatus(talk.getId());
        }

        // 保存图片列表信息
        if (talkDto.getImagesList() != null && talkDto.getImagesList().size() > 0) {
            fileRelationMapper.deleteByBusinessId(talk.getId());
            talkDto.getImagesList().forEach(image -> {
                FileRelation fileRelation = new FileRelation();
                fileRelation.setId(GenerateIdUtils.getId());
                fileRelation.setFileId(image.getId());
                fileRelation.setBusinessId(talk.getId());
                fileRelationMapper.insert(fileRelation);
            });
        }
    }

    /**
     * 删除说说
     *
     * @param talkId 说说ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTalk(Long talkId) {
        Assert.notNull(talkId, "参数【talkId】不能为空");
        Talk dbTalk = baseMapper.selectById(talkId);
        Assert.notNull(dbTalk, "删除失败，数据不存在");
        String loginUsername = SecurityUtils.getLoginUsername();
        DataValidated.isTrue(!loginUsername.equals(dbTalk.getCreateUser()), "没有权限删除他人的说说");
        Assert.isTrue(baseMapper.deleteById(talkId) != 1, "删除失败");
    }

    private void checkTalk(Talk talk) {
        // 只有创建人有修改权限
        String loginUsername = SecurityUtils.getLoginUsername();
        if (talk.getId() != null) {
            Talk dbTalk = baseMapper.selectById(talk.getId());
            Assert.notNull(dbTalk, "修改失败，数据不存在");
            DataValidated.isTrue(!loginUsername.equals(dbTalk.getCreateUser()), "没有权限修改他人的说说");
        } else {
            talk.setId(GenerateIdUtils.getId());
        }

        Assert.notNull(talk.getContent(), "说说内容不能为空");
        Assert.notNull(TalkStatus.of(talk.getStatus()), "说说状态不正确，取值范围[1-公开, 2-私密]");
    }
}
