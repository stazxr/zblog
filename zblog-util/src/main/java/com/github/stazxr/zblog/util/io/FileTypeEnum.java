package com.github.stazxr.zblog.util.io;

import java.util.Arrays;

/**
 * 文件类型处理
 *
 * @author SunTao
 * @since 2022-07-29
 */
enum FileTypeEnum {
    /**
     * 图片
     */
    IMAGE("图片", "image", new String[]{
        "bmp", "dib", "pcp", "dif", "wmf", "gif", "jpg", "tif", "eps", "psd",
        "cdr", "iff", "tga", "pcd", "mpt", "png", "jpeg", "webp"
    }),

    /**
     * 文档
     */
    TXT("文档", "txt", new String[]{
        "txt", "doc", "pdf", "ppt", "pps", "xlsx", "xls", "docx"
    }),

    /**
     * 音乐
     */
    MUSIC("音乐", "music", new String[]{
        "mp3", "wav", "wma", "mpa", "ram", "ra", "aac", "aif", "m4a"
    }),

    /**
     * 视频
     */
    VIDEO("视频", "video", new String[]{
        "avi", "mpg", "mpe", "mpeg", "asf", "wmv", "mov", "qt", "rm", "mp4", "flv", "m4v", "webm", "ogv", "ogg"
    }),

    /**
     * 其他
     */
    OTHER("其他", "other", new String[]{""});

    private final String type;

    private final String enType;

    private final String[] values;

    FileTypeEnum(String type, String enType, String[] values) {
        this.type = type;
        this.enType = enType;
        this.values = values;
    }

    /**
     * 获取文件类型
     *
     * @param suffix 文件后缀
     * @return 图片、文档、音乐、视频、其他
     */
    public static String matchFileType(String suffix) {
        for (FileTypeEnum typeHandler : values()) {
            if (typeHandler.contains(suffix)) {
                return typeHandler.type;
            }
        }

        return OTHER.type;
    }

    /**
     * 获取英文文件类型
     *
     * @param suffix 文件后缀
     * @return 图片、文档、音乐、视频、其他
     */
    public static String matchFileEnType(String suffix) {
        for (FileTypeEnum typeHandler : values()) {
            if (typeHandler.contains(suffix)) {
                return typeHandler.enType;
            }
        }

        return OTHER.enType;
    }

    boolean contains(String suffix) {
        return Arrays.asList(values).contains(suffix);
    }
}
