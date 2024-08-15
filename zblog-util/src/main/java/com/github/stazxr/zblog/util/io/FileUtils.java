package com.github.stazxr.zblog.util.io;

import com.github.stazxr.zblog.util.secret.Md5Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * IoUtils
 *
 * @author SunTao
 * @since 2022-06-14
 */
public class FileUtils {
    /**
     * 系统临时目录
     *   windows : C:\Users/xxx\AppData\Local\Temp\
     *   linux: /tmp
     */
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;

    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;

    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * KB;

    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * MB;

    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 读取文件
     *
     * @param filepath 文件路径
     * @param encode   文件编码
     * @return 文件内容
     * @throws IOException 读取异常
     */
    public static String readFile(String filepath, String encode) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filepath)), encode);
    }

    /**
     * 从流中读取文件内容
     *
     * @param inputStream 输入流
     * @return 字符串
     * @throws IOException 读取异常
     */
    public static String readFileFromStream(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            return out.toString();
        }
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 字符串
     * @throws IOException 读取异常
     */
    public static String readFile(File file) throws IOException {
        try (InputStream in = Files.newInputStream(file.toPath()); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            return out.toString();
        }
    }

    /**
     * 将文件转换成Byte数组
     *
     * @param file 文件
     * @return Byte数组
     * @throws Exception 文件读取异常
     */
    public static byte[] readFileBytes(File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)) {
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        }
    }

    /**
     * 写入文件
     *
     * @param data   文件内容
     * @param file   输出文件
     * @param encode 文件编码
     * @throws IOException 写入异常
     */
    public static void writeFile(String data, String file, String encode) throws IOException {
        Path outPath = Paths.get(file);
        if (!Files.exists(outPath.getParent())) {
            Files.createDirectories(outPath.getParent());
        }
        Files.write(outPath, data.getBytes(encode));
    }

    /**
     * 写入文件
     *
     * @param data   文件内容
     * @param file   输出文件
     * @throws IOException 写入异常
     */
    public static void writeFile(String data, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath())))) {
            writer.write(data);
            writer.flush();
        }
    }

    /**
     * 写入文件
     *
     * @param data    文件内容
     * @param file    输出文件
     * @param charset 文件编码
     * @throws IOException 写入异常
     */
    public static void writeFile(String data, String file, Charset charset) throws IOException {
        writeFile(data, new File(file), charset);
    }

    /**
     * 写入文件
     *
     * @param data    文件内容
     * @param file    输出文件
     * @param charset 文件编码
     * @throws IOException 写入异常
     */
    public static void writeFile(String data, File file, Charset charset) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), charset))) {
            writer.write(data);
            writer.flush();
        }
    }

    /**
     * 将Byte数组转换成文件
     *
     * @param bytes 字节数组
     * @param fileFullPath 文件路径
     * @throws Exception IO异常
     */
    public static void writeFile(byte[] bytes, String fileFullPath) throws Exception {
        File dest = new File(fileFullPath).getCanonicalFile();
        if (!dest.getParentFile().exists() && !dest.getParentFile().mkdirs()) {
            throw new IOException("目录创建失败，" + dest.getParentFile().getAbsolutePath());
        }

        try (FileOutputStream fos = new FileOutputStream(dest); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bytes);
        }
    }

    /**
     * 获取文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            int dot = filePath.lastIndexOf('/');
            if (dot != -1) {
                return filePath.substring(dot + 1);
            } else {
                return filePath;
            }
        }
        return "";
    }

    /**
     * 获取文件名（无后缀）
     *
     * @param filename 文件名
     * @return 文件名
     */
    public static String getFileNameNoEx(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot != -1) {
                return filename.substring(0, dot);
            } else {
                return filename;
            }
        }
        return "";
    }

    /**
     * 获取文件后缀
     *
     * @param filename 文件名
     * @return 文件后缀
     */
    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot != -1) {
                return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
            } else {
                return "";
            }
        }
        return "";
    }

    /**
     * 获取文件类型
     *
     * @param suffix 文件后缀
     * @return FileTypeEnum.values(), if no match, return OTHER
     */
    public static String getFileType(String suffix) {
        suffix = suffix == null ? "" : suffix.toLowerCase(Locale.ROOT);
        return FileTypeEnum.matchFileType(suffix);
    }

    /**
     * 获取文件大小
     *
     * @param size longSize
     * @return 文件大小描述
     */
    public static String parseSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B";
        }
        return resultSize;
    }

    /**
     * 获取文件类型，英文描述
     *
     * @param suffix 文件后缀
     * @return FileTypeEnum.values(), if no match, return OTHER
     */
    public static String getFileEnType(String suffix) {
        suffix = suffix == null ? "" : suffix.toLowerCase(Locale.ROOT);
        return FileTypeEnum.matchFileEnType(suffix);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file 文件
     * @return MD5
     */
    public static String getFileMd5(File file) throws Exception {
        return Md5Utils.getMessageDigest(readFileBytes(file));
    }

    /**
     * 格式化文件大小
     *
     * @param size Long
     * @return XXX GB...
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 验证并过滤非法的文件名
     *
     * @param fileName 文件名
     * @return 新文件名
     */
    public static String verifyFilename(String fileName) {
        if (fileName == null) {
            return null;
        }

        // 过滤掉特殊字符
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|~\\s]", "");

        // 去掉文件名开头和结尾的空格和点
        fileName = fileName.trim().replaceAll("^[. ]+|[. ]+$", "");

        // 不允许文件名超过255（在Mac和Linux中）或260（在Windows中）个字符
        int maxFileNameLength = 255;
        if (fileName.length() > maxFileNameLength) {
            fileName = fileName.substring(0, maxFileNameLength);
        }

        // 过滤掉控制字符
        fileName = fileName.replaceAll("\\p{Cntrl}", "");

        // 过滤掉 ".." 路径
        fileName = fileName.replaceAll("\\.{2,}", "");

        // 去掉文件名开头的 ".."
        fileName = fileName.replaceAll("^\\.+/", "");

        // 保留文件名中最后一个 "." 字符，过滤掉其他 "."
        fileName = fileName.replaceAll("^(.*)(\\.[^.]*)$", "$1").replaceAll("\\.", "") + fileName.replaceAll("^(.*)(\\.[^.]*)$", "$2");
        return fileName;
    }

    /**
     * 确保文件路径存在，不存在则创建
     *
     * @param file 文件
     */
    public static void ensureFileDirExist(File file) {
        if (file == null || file.exists()) {
            return;
        }

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new RuntimeException("文件路径不存在，且创建失败！");
            }
        }
    }
}
