package com.github.stazxr.zblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.util.GenerateIdUtils;
import com.github.stazxr.zblog.converter.AlbumConverter;
import com.github.stazxr.zblog.core.util.DataValidated;
import com.github.stazxr.zblog.core.util.SecurityUtils;
import com.github.stazxr.zblog.domain.dto.AlbumDto;
import com.github.stazxr.zblog.domain.dto.AlbumPhotoDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumPhotoQueryDto;
import com.github.stazxr.zblog.domain.dto.query.AlbumQueryDto;
import com.github.stazxr.zblog.domain.entity.Album;
import com.github.stazxr.zblog.domain.entity.AlbumPhoto;
import com.github.stazxr.zblog.domain.vo.AlbumPhotoVo;
import com.github.stazxr.zblog.domain.vo.AlbumVo;
import com.github.stazxr.zblog.mapper.AlbumMapper;
import com.github.stazxr.zblog.mapper.AlbumPhotoMapper;
import com.github.stazxr.zblog.service.AlbumService;
import com.github.stazxr.zblog.util.Assert;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.time.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 说说服务实现层
 *
 * @author SunTao
 * @since 2022-12-15
 */
@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {
    private final AlbumPhotoMapper albumPhotoMapper;

    private final AlbumConverter albumConverter;

    /**
     * 分页查询相册列表
     *
     * @param queryDto 查询参数
     * @return AlbumVoList
     */
    @Override
    public PageInfo<AlbumVo> pageAlbumList(AlbumQueryDto queryDto) {
        // 查询当前用户信息
        String loginUsername = SecurityUtils.getLoginUsername();
        queryDto.setLoginUser(loginUsername);

        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(baseMapper.selectAlbumList(queryDto));
    }

    /**
     * 查询相册详情
     *
     * @param albumId 相册ID
     * @return AlbumVo
     */
    @Override
    public AlbumVo queryAlbumDetail(Long albumId) {
        Assert.notNull(albumId, "参数【albumId】不能为空");
        AlbumVo albumVo = baseMapper.selectAlbumDetail(albumId);
        Assert.notNull(albumVo, "查询相册信息失败，说说【" + albumId + "】不存在");
        return albumVo;
    }

    /**
     * 新增或编辑相册
     *
     * @param albumDto 相册信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrEditAlbum(AlbumDto albumDto) {
        Album album = albumConverter.dtoToEntity(albumDto);
        checkAlbum(album);
        Assert.nonTrue(saveOrUpdate(album), "操作失败");
    }

    /**
     * 删除相册
     *
     * @param albumId 相册ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlbum(Long albumId) {
        Assert.notNull(albumId, "参数【albumId】不能为空");
        Album dbAlbum = baseMapper.selectById(albumId);
        Assert.notNull(dbAlbum, "删除失败，数据不存在");
        String loginUsername = SecurityUtils.getLoginUsername();
        DataValidated.isTrue(!loginUsername.equals(dbAlbum.getCreateUser()), "没有权限删除他人的相册");
        Assert.isTrue(baseMapper.deleteById(albumId) != 1, "删除失败");
    }

    /**
     * 分页查询照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    @Override
    public PageInfo<AlbumPhotoVo> pagePhotoList(AlbumPhotoQueryDto queryDto) {
        Assert.notNull(queryDto.getAlbumId(), "参数【albumId】不能为空");

        queryDto.checkPage();
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(albumPhotoMapper.selectAlbumPhotoList(queryDto));
    }

    /**
     * 上传相册照片
     *
     * @param albumPhotoDto 相册照片信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAlbumPhoto(AlbumPhotoDto albumPhotoDto) {
        Assert.notNull(albumPhotoDto.getAlbumId(), "参数【albumId】不能为空");
        Assert.isTrue(albumPhotoDto.getPhotoList().size() == 0, "请选择需要上传的照片");

        // 批量插入照片列表（暂不限制单次上传大小）
        String loginUsername = SecurityUtils.getLoginUsername();
        List<AlbumPhoto> photos = new ArrayList<>();
        for (File file : albumPhotoDto.getPhotoList()) {
            AlbumPhoto albumPhoto = new AlbumPhoto();
            albumPhoto.setId(GenerateIdUtils.getId());
            albumPhoto.setAlbumId(albumPhotoDto.getAlbumId());
            albumPhoto.setFileId(file.getId());
            albumPhoto.setPhotoName(file.getFilename());
            albumPhoto.setPhotoLink(file.getDownloadUrl());
            albumPhoto.setIsDeleted(false);
            albumPhoto.setCreateUser(loginUsername);
            albumPhoto.setCreateDate(DateUtils.formatDate());
            albumPhoto.setCreateTime(DateUtils.formatTime());
            photos.add(albumPhoto);
        }

        albumPhotoMapper.batchInsert(photos);
    }

    /**
     * 移动相册照片
     *
     * @param albumPhotoDto 相册照片信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveAlbumPhoto(AlbumPhotoDto albumPhotoDto) {
        List<Long> photoIdList = albumPhotoDto.getPhotoIdList();
        Assert.notNull(albumPhotoDto.getAlbumId(), "请选择目标相册");
        Assert.isTrue(photoIdList == null || photoIdList.size() == 0, "请选择需要移动的照片");
        albumPhotoMapper.moveAlbumPhoto(albumPhotoDto.getAlbumId(), photoIdList);
    }

    /**
     * 删除相册照片
     *
     * @param albumPhotoDto 相册照片信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlbumPhoto(AlbumPhotoDto albumPhotoDto) {
        List<Long> photoIdList = albumPhotoDto.getPhotoIdList();
        if (photoIdList != null && photoIdList.size() > 0) {
            albumPhotoMapper.deleteAlbumPhoto(photoIdList);
        }
    }

    /**
     * 查询用户相册列表
     *
     * @param queryDto 查询参数
     * @return AlbumVoList
     */
    @Override
    public List<AlbumVo> queryUserAlbumList(AlbumQueryDto queryDto) {
        Assert.notNull(queryDto.getLoginUserId(), "查询相册列表失败");
        return baseMapper.selectUserAlbumList(queryDto);
    }

    /**
     * 分页查询回收站照片列表
     *
     * @param queryDto 查询参数
     * @return PhotoVoList
     */
    @Override
    public PageInfo<AlbumPhotoVo> pageDeletePhotoList(AlbumPhotoQueryDto queryDto) {
        queryDto.checkPage();
        queryDto.setUserId(SecurityUtils.getLoginId());
        PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize());
        return new PageInfo<>(albumPhotoMapper.selectDeletePhotoList(queryDto));
    }

    /**
     * 永久删除相册照片
     *
     * @param albumPhotoDto 相册照片信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlbumPhotoForever(AlbumPhotoDto albumPhotoDto) {
        List<Long> photoIdList = albumPhotoDto.getPhotoIdList();
        if (photoIdList != null && photoIdList.size() > 0) {
            albumPhotoMapper.deleteBatchIds(photoIdList);
        }
    }

    /**
     * 恢复相册照片
     *
     * @param albumPhotoDto 相册照片信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recoverAlbumPhoto(AlbumPhotoDto albumPhotoDto) {
        List<Long> photoIdList = albumPhotoDto.getPhotoIdList();
        if (photoIdList != null && photoIdList.size() > 0) {
            albumPhotoMapper.recoverAlbumPhoto(photoIdList);
        }
    }

    private void checkAlbum(Album album) {
        // 只有创建人有修改权限
        String loginUsername = SecurityUtils.getLoginUsername();
        if (album.getId() != null) {
            Album dbAlbum = baseMapper.selectById(album.getId());
            Assert.notNull(dbAlbum, "修改失败，数据不存在");
            DataValidated.isTrue(!loginUsername.equals(dbAlbum.getCreateUser()), "没有权限修改他人的相册");
        } else {
            album.setId(GenerateIdUtils.getId());
        }

        Assert.isTrue(StringUtils.isBlank(album.getAlbumName()), "相册名称不能为空");
        Assert.notNull(album.getStatus(), "相册状态不能为空");
    }
}
