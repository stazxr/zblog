package com.github.stazxr.zblog.base.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.stazxr.zblog.bas.encryption.util.Md5Utils;
import com.github.stazxr.zblog.bas.exception.BaseException;
import com.github.stazxr.zblog.bas.exception.ExpMessageCode;
import com.github.stazxr.zblog.bas.file.FileHandler;
import com.github.stazxr.zblog.bas.file.FileHandlerEnum;
import com.github.stazxr.zblog.bas.file.autoconfigure.FileProperties;
import com.github.stazxr.zblog.bas.file.model.FileInfo;
import com.github.stazxr.zblog.bas.security.SecurityUtils;
import com.github.stazxr.zblog.bas.sequence.util.SequenceUtils;
import com.github.stazxr.zblog.bas.validation.Assert;
import com.github.stazxr.zblog.base.domain.bo.storage.BaseStorageConfig;
import com.github.stazxr.zblog.base.domain.dto.query.FileQueryDto;
import com.github.stazxr.zblog.base.domain.entity.File;
import com.github.stazxr.zblog.base.domain.entity.FileStorage;
import com.github.stazxr.zblog.base.domain.entity.User;
import com.github.stazxr.zblog.base.domain.vo.FileVo;
import com.github.stazxr.zblog.base.domain.vo.UploadFileVo;
import com.github.stazxr.zblog.base.mapper.DictMapper;
import com.github.stazxr.zblog.base.mapper.FileMapper;
import com.github.stazxr.zblog.base.mapper.FileStorageMapper;
import com.github.stazxr.zblog.base.service.FileService;
import com.github.stazxr.zblog.core.exception.ServiceException;
import com.github.stazxr.zblog.util.StringUtils;
import com.github.stazxr.zblog.util.io.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;

    private final FileStorageMapper fileStorageMapper;

    private final DictMapper dictMapper;

    private final FileProperties fileProperties;

    private final MultipartProperties multipartProperties;

    /**
     * 分页查询文件列表
     *
     * @param queryDto 查询参数
     * @return FileList
     */
    @Override
    public PageInfo<FileVo> queryFileListByPage(FileQueryDto queryDto) {
        // 参数检查
        queryDto.checkPage();
        // 分页查询
        try (Page<FileVo> page = PageHelper.startPage(queryDto.getPage(), queryDto.getPageSize())) {
            List<FileVo> dataList = fileMapper.selectFileList(queryDto);
            return page.doSelectPageInfo(() -> new PageInfo<>(dataList));
        }
    }

    /**
     * 获取文件上传模式
     *
     * @return 文件上传模式
     */
    @Override
    public int getFileUploadModel() {
        return fileProperties.getModel();
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
        Assert.failIfTrue(hasNoFileFlag, ExpMessageCode.of("valid.file.upload.chooseNoneFile"));
        if (multipartFiles == null || multipartFiles.length < 1) {
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
        Assert.notNull(multipartFile, ExpMessageCode.of("valid.file.upload.chooseNoneFile"));

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
        Assert.notNull(fileVo, ExpMessageCode.of("valid.file.common.fileNotExist"));

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
        fileHandler.downloadFile(fileVo.getFileAbsolutePath(), response);
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
        Assert.notNull(fileVo, ExpMessageCode.of("valid.file.common.fileNotExist"));

        // 判断文件是否关联业务
        Assert.isNull(fileVo.getBusinessId(), ExpMessageCode.of("valid.file.delete.hasBusiness"));

        // 删除逻辑文件
        Assert.affectOneRow(fileMapper.deleteById(fileId), ExpMessageCode.of("valid.file.delete.logicFailed"));

        // 判断是否需要删除物理文件
        Integer referenceCount = fileVo.getReferenceCount();
        boolean hasReference = referenceCount == null || --referenceCount > 0;
        if (!hasReference) {
            // 删除物理文件表数据
            Assert.affectOneRow(fileStorageMapper.deleteById(fileVo.getFileStorageId()), ExpMessageCode.of("valid.file.delete.storageFailed"));

            // 删除物理文件
            FileHandler fileHandler = FileHandlerEnum.instance(fileVo.getUploadType());
            fileHandler.deleteFile(fileVo.getFileAbsolutePath());
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
                // 判断文件是否已经上传过存储服务器
                String fileMd5 = Md5Utils.md5(_multipartFile.getInputStream());
                FileStorage dbFileStorage = fileStorageMapper.selectFileByMd5(fileMd5);
                UploadFileVo uploadFileVo;
                if (dbFileStorage == null) {
                    // 首次上传
                    uploadFileVo = firstUploadFile(fileHandler, _multipartFile, uploadSuccessFileList);
                } else {
                    // 非首次上传
                    uploadFileVo = insertFileInfoToDb(null, dbFileStorage, _multipartFile);
                }
                uploadFileVoList.add(uploadFileVo);
            } catch (Exception e) {
                // 文件信息入库失败，需要删除已上传成功的文件
                try {
                    fileHandler.deleteFiles(uploadSuccessFileList);
                } catch (Exception _e) {
                    log.error("文件删除失败：{}", uploadSuccessFileList, _e);
                }
                throw e;
            }
        }

        return uploadFileVoList;
    }

    private void fileUploadPreCheck(MultipartFile[] multipartFiles) {
        // 开关检查
        boolean enabled = multipartProperties.getEnabled();
        Assert.failIfFalse(enabled, ExpMessageCode.of("valid.file.upload.switchOff"));
        // 上传文件检查
        if (multipartFiles != null && multipartFiles.length > 0) {
            List<String> whiteList = dictMapper.selectDictValuesByDictKey("FILE_UPLOAD_WHITE_LIST");
            for (MultipartFile multipartFile : multipartFiles) {
                // TODO 文件类型检查
                String contentType = multipartFile.getContentType();
                boolean isImage = contentType != null && contentType.startsWith("image/");
                boolean isVideo = contentType != null && contentType.startsWith("video/");
                Assert.failIfTrue(!isImage && !isVideo, ExpMessageCode.of("valid.file.upload.notSupport", contentType));

                // TODO 白名单检查
                String suffix = FileUtils.getExtensionName(multipartFile.getOriginalFilename());
                boolean isNotWhiteList = StringUtils.isBlank(suffix) || !whiteList.contains(suffix.toLowerCase(Locale.ROOT));
                Assert.failIfTrue(isNotWhiteList, ExpMessageCode.of("valid.file.upload.notWhiteList", suffix));

                // 图片合法性校验
                if (contentType.startsWith("image/")) {
                    try {
                        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
                        Assert.notNull(image, ExpMessageCode.of("valid.file.upload.imageInvalid"));
                        boolean imageSizeValid = image.getWidth() <= 4000 && image.getHeight() <= 4000;
                        Assert.failIfFalse(imageSizeValid, ExpMessageCode.of("valid.file.upload.imageRadioMax"));
                    } catch (BaseException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new ServiceException(ExpMessageCode.of("valid.file.upload.imageInvalid"));
                    }
                }

                // 文件大小检查
                boolean sizeInvalid = multipartFile.getSize() > multipartProperties.getMaxFileSize().toBytes();
                Assert.failIfTrue(sizeInvalid, ExpMessageCode.of("valid.file.upload.sizeOverMax", multipartProperties.getMaxFileSize().toBytes()));
            }
        }
    }

    private UploadFileVo firstUploadFile(FileHandler fileHandler, MultipartFile multipartFile, List<String> filepathList) {
        // 调用API上传文件到存储服务器
        FileInfo fileInfo = fileHandler.uploadFile(multipartFile);
        filepathList.add(fileInfo.getFileAbsolutePath()); // 文件上传成功
        return insertFileInfoToDb(fileInfo, null, multipartFile);
    }

    private UploadFileVo insertFileInfoToDb(FileInfo fileInfo, FileStorage fileStorage, MultipartFile multipartFile) {
        // 获取当前登录用户信息，用户只有登录后才能上传图片
        User user = SecurityUtils.getLoginUser();
        try {
            if (fileStorage == null) {
                // 首次上传，已经获取了 FileInfo
                Date uploadTime = new Date();
                fileStorage = new FileStorage(fileInfo);
                fileStorage.setUploadUser(user.getId());
                fileStorage.setUploadTime(uploadTime);
                fileStorageMapper.insert(fileStorage);
            }
            // 获取文件信息
            Long fileId = SequenceUtils.getId();
            String filename = String.valueOf(fileId);
            String originalFilename = FileUtils.verifyFilename(multipartFile.getOriginalFilename());
            String fileSuffix = FileUtils.getExtensionName(originalFilename);
            if (StringUtils.isNotBlank(fileSuffix)) {
                filename = filename + "." + fileSuffix;
            }
            // 存储逻辑文件表
            File file = new File();
            file.setId(fileId);
            file.setStorageId(fileStorage.getId());
            file.setFilename(filename);
            file.setOriginalFilename(originalFilename);
            file.setCreateUser(user.getId());
            file.setCreateTime(new Date());
            fileMapper.insert(file);
            // 返回文件信息
            UploadFileVo uploadFileVo = new UploadFileVo();
            uploadFileVo.setFileId(fileId);
            uploadFileVo.setFilename(file.getFilename());
            uploadFileVo.setOriginalFilename(file.getOriginalFilename());
            uploadFileVo.setFileAbsolutePath(fileStorage.getFileAbsolutePath());
            uploadFileVo.setFileRelativePath(fileStorage.getFileRelativePath());
            uploadFileVo.setDownloadUrl(fileStorage.getDownloadUrl());
            return uploadFileVo;
        } catch (Exception e) {
            throw new ServiceException(ExpMessageCode.of("valid.file.upload.insertDbFailed"), e);
        }
    }








    /**
     * 获取配置信息
     *
     * @param storageType 存储类型
     * @return BaseStorageConfig
     */
    @Override
    public BaseStorageConfig getConfigInfo(Integer storageType) {
        return null;
//        Assert.notNull(storageType, "参数【storageType】不能为空");
//        if (FileHandlerEnum.LOCAL.getType() == storageType) {
//            // 本地存储配置从配置文件获取
//            LocalConfig config = new LocalConfig();
//            config.setDomain(zblogProperties.getFileBaseUrl());
//            config.setUploadFolder(zblogProperties.getFileUploadPath());
//            return config;
//        } else if (FileHandlerEnum.ALI_YUN.getType() == storageType) {
//            String configValue = dictMapper.selectSingleValue(BaseConst.DictKey.CLOUD_STORAGE_A_LI_CONFIG);
//            AliYunConfig config = AliYunConfig.instanceFromJson(configValue);
//            config.setAccessKeySecret(decodeSecret(config.getAccessKeySecret()));
//            return config;
//        } else if (FileHandlerEnum.QI_NIU_YUN.getType() == storageType) {
//            String configValue = dictMapper.selectSingleValue(BaseConst.DictKey.CLOUD_STORAGE_QI_NIU_CONFIG);
//            QiNiuYunConfig config = QiNiuYunConfig.instanceFromJson(configValue);
//            config.setSk(decodeSecret(config.getSk()));
//            return config;
//        } else {
//            throw new BadConfigurationException("不支持的存储类型：" + storageType);
//        }
    }
}
