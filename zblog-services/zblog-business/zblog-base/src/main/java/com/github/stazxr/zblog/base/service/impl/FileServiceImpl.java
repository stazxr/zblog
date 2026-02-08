package com.github.stazxr.zblog.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.SystemException;
import com.github.stazxr.zblog.bas.exception.ThrowUtils;
import com.github.stazxr.zblog.bas.file.UploadContext;
import com.github.stazxr.zblog.bas.file.UploadContextFactory;
import com.github.stazxr.zblog.bas.file.autoconfigure.properties.FileProperties;
import com.github.stazxr.zblog.bas.file.handler.FileHandler;
import com.github.stazxr.zblog.bas.file.handler.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.file.path.FilePathContext;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.domain.entity.FileStorage;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.error.FileErrorCode;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileStorageMapper;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.core.base.BaseErrorCode;
import com.github.stazxr.zblog.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.net.URLEncoder;
import java.util.*;

/**
 * 存储管理业务层
 *
 * @author SunTao
 * @since 2022-07-27
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileMapper fileMapper;

    private final FileStorageMapper fileStorageMapper;

    private final DictMapper dictMapper;

    private final FileProperties fileProperties;

    private final MultipartProperties multipartProperties;

    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return IPage<FileVo>
     */
    @Override
    public IPage<FileVo> queryFileListByPage(FileQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        // 分页查询
        Page<FileVo> page = new Page<>(queryDto.getPage(), queryDto.getPageSize());
        return fileMapper.selectFileList(page, queryDto);
    }

    /**
     * 文件上传，支持单文件，多文件上传
     *
     * @param multipartFile  单文件上传
     * @param multipartFiles 多文件上传
     * @return List<UploadFileVo>
     * @throws Exception 文件上传失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UploadFileVo> uploadFile(MultipartFile multipartFile, MultipartFile[] multipartFiles) throws Exception {
        // 检查是否选择上传文件
        boolean hasNoFileFlag = (multipartFiles == null || multipartFiles.length < 1) && multipartFile == null;
        ThrowUtils.throwIf(hasNoFileFlag, FileErrorCode.EFILEB001);
        if (multipartFiles == null || multipartFiles.length == 0) {
            multipartFiles = new MultipartFile[] { multipartFile };
        }

        // 获取文件上传类型
        int fileUploadType = fileProperties.getModel();

        // 上传操作
        return doUploadFile(multipartFiles, fileUploadType);
    }

    /**
     * 测试文件上传
     *
     * @param multipartFile 上传文件
     * @param uploadType    上传类型
     * @return UploadFileVo
     * @throws Exception 文件上传失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadFileVo uploadFileTest(MultipartFile multipartFile, Integer uploadType) throws Exception {
        // 检查是否选择上传文件
        ThrowUtils.throwIfNull(multipartFile, FileErrorCode.EFILEB001);

        // 上传文件
        MultipartFile[] multipartFiles = new MultipartFile[] { multipartFile };
        List<UploadFileVo> uploadFileVoList = doUploadFile(multipartFiles, uploadType);
        return uploadFileVoList.get(0);
    }

    /**
     * 下载文件
     *
     * @param fileId   文件id
     * @param isDown   是否强制下载
     * @param response 响应对象
     * @throws Exception 文件下载失败
     */
    @Override
    public void downloadFile(Long fileId, Boolean isDown, HttpServletResponse response) throws Exception {
        // 获取文件信息
        FileVo fileVo = fileMapper.selectFileDetailById(fileId);
        ThrowUtils.throwIfNull(fileVo, FileErrorCode.EFILEB000);

        // 设置响应头
        String encodeFilename = URLEncoder.encode(fileVo.getOriginalFilename(), "UTF-8");
        if (isDown == null || !isDown) {
            // inline
            response.addHeader("Content-Disposition", "inline;fileName=" + encodeFilename);
        } else {
            // download
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + encodeFilename);
        }

        // 下载文件
        FileHandler fileHandler = FileHandlerEnum.instance(fileVo.getUploadType());
        fileHandler.download(fileVo.getFileAbsolutePath(), response);
    }

    /**
     * 删除文件<br/>
     * <br/>
     * 1. 文件必须存在<br/>
     * 2. 文件不能有关联业务<br/>
     * 3. 必须成功删除逻辑文件<br/>
     * 4. 若物理文件仍有引用 → 禁止删除<br/>
     * 5. 物理文件记录删除失败 → 报错<br/>
     * 6. 最后才真正删除物理文件<br/>
     * <br/>
     * @param fileId 文件id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long fileId) {
        // 获取文件信息
        FileVo fileVo = fileMapper.selectFileDetailById(fileId);
        ThrowUtils.throwIfNull(fileVo, FileErrorCode.EFILEB000);

        // 判断文件是否关联业务
        ThrowUtils.throwIf(fileVo.getBusinessId() != null, FileErrorCode.EFILEB007);

        // 删除逻辑文件
        ThrowUtils.when(fileMapper.deleteById(fileId) != 1).system(BaseErrorCode.SCOREA003);

        // 判断是否需要删除物理文件
        Integer referenceCount = fileVo.getReferenceCount();
        boolean hasReference = referenceCount == null || --referenceCount > 0;
        if (!hasReference) {
            // 删除物理文件表数据
            ThrowUtils.when(fileStorageMapper.deleteById(fileVo.getFileStorageId()) != 1).system(BaseErrorCode.SCOREA003);

            // 删除物理文件
            FileHandler fileHandler = FileHandlerEnum.instance(fileVo.getUploadType());
            fileHandler.delete(fileVo.getFileAbsolutePath());
        }
    }

    private List<UploadFileVo> doUploadFile(MultipartFile[] multipartFiles, int fileUploadType) throws Exception {
        // 获取文件上传处理器
        FileHandler fileHandler = FileHandlerEnum.instance(fileUploadType);

        // 前置检查
        fileUploadPreCheck(multipartFiles);

        // 上传文件到存储服务器
        List<UploadFileVo> uploadFileVoList = new ArrayList<>();
        List<String> uploadSuccessFileList = new ArrayList<>();
        for (MultipartFile _multipartFile : multipartFiles) {
            try {
                try (UploadContext ctx = UploadContextFactory.from(_multipartFile, FilePathContext.empty())) {
                    FileStorage dbFileStorage = fileStorageMapper.selectFileByMd5(ctx.getMd5(), fileUploadType);
                    if (dbFileStorage != null) {
                        // 复用已上传的文件
                        uploadFileVoList.add(insertFileInfoToDb(false, dbFileStorage, ctx));
                    } else {
                        // 上传文件
                        FileInfo fileInfo = fileHandler.upload(ctx);
                        uploadSuccessFileList.add(fileInfo.getStorageLocation());
                        FileStorage fileStorage = new FileStorage(fileInfo);
                        uploadFileVoList.add(insertFileInfoToDb(true, fileStorage, ctx));
                    }
                }
            } catch (Exception e) {
                // 文件信息入库失败，需要删除已上传成功的文件
                for (String storageLocation : uploadSuccessFileList) {
                    try {
                        fileHandler.delete(storageLocation);
                    } catch (Exception _e) {
                        log.error("文件删除失败：{}", storageLocation, _e);
                    }
                }
                throw e;
            }
        }

        return uploadFileVoList;
    }

    private UploadFileVo insertFileInfoToDb(boolean firstUpload, FileStorage fileStorage, UploadContext uploadContext) {
        // 获取当前登录用户信息，用户只有登录后才能上传图片
        User user = SecurityUtils.getLoginUser();

        try {
            if (firstUpload) {
                // 入库 file_storage
                fileStorage.setUploadUser(user.getId()); // 记录上传用户
                fileStorageMapper.insert(fileStorage);
            }

            // 入库 file
            File file = new File();
            file.setId(SequenceUtils.getId());
            file.setStorageId(fileStorage.getId());
            file.setOriginalFilename(uploadContext.getOriginalFilename());
            file.setCreateUser(user.getId());
            file.setCreateTime(new Date());
            fileMapper.insert(file);

            // 返回上传文件信息
            return newUploadFileVo(fileStorage, file);
        } catch (Exception e) {
            throw new SystemException(FileErrorCode.SFILEB001, e);
        }
    }

    private UploadFileVo newUploadFileVo(FileStorage fileStorage, File file) {
        UploadFileVo uploadFileVo = new UploadFileVo();
        uploadFileVo.setFileId(file.getId());
        uploadFileVo.setOriginalFilename(file.getOriginalFilename());
        uploadFileVo.setFilename(fileStorage.getFilename());
        uploadFileVo.setFileAbsolutePath(fileStorage.getFileAbsolutePath());
        uploadFileVo.setFileRelativePath(fileStorage.getFileRelativePath());
        uploadFileVo.setFileAccessUrL(fileStorage.getFileAccessUrl());
        return uploadFileVo;
    }

    private void fileUploadPreCheck(MultipartFile[] multipartFiles) {
        // 开关检查
        boolean enabled = multipartProperties.getEnabled();
        ThrowUtils.throwIf(!enabled, FileErrorCode.EFILEB002);
        // 上传文件检查
        if (multipartFiles != null && multipartFiles.length > 0) {
            List<String> whiteList = dictMapper.selectDictValuesByDictKey("FILE_UPLOAD_WHITE_LIST");
            for (MultipartFile multipartFile : multipartFiles) {
                // 文件类型检查
                String contentType = multipartFile.getContentType();
                boolean isNotWhiteList = StringUtils.isBlank(contentType) || !whiteList.contains(contentType.toLowerCase(Locale.ROOT));
                ThrowUtils.throwIf(isNotWhiteList, FileErrorCode.EFILEB003, contentType);
                // 图片合法性校验
                if (contentType.startsWith("image/")) {
                    try {
                        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
                        ThrowUtils.throwIfNull(image, FileErrorCode.EFILEB004);
                        /* FIX: 不检验文件分辨率 boolean imageSizeValid = image.getWidth() <= 4000 && image.getHeight() <= 4000;
                        ThrowUtils.throwIf(!imageSizeValid, FileErrorCode.EFILEB005); */
                    } catch (BaseException e) {
                        throw e;
                    } catch (Exception e) {
                        ThrowUtils.service(FileErrorCode.EFILEB004, e);
                    }
                }
                // 文件大小检查
                long maxFileSize = multipartProperties.getMaxFileSize().toBytes();
                boolean sizeInvalid = multipartFile.getSize() > maxFileSize;
                ThrowUtils.throwIf(sizeInvalid, FileErrorCode.EFILEB006, maxFileSize);
            }
        }
    }
}
