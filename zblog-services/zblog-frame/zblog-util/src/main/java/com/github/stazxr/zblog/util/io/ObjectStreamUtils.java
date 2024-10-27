package com.github.stazxr.zblog.util.io;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 用于将对象序列化到文件和从文件反序列化对象。

 * @author SunTao
 * @since 2022-07-29
 */
public class ObjectStreamUtils {

    /**
     * 将指定对象序列化并写入到指定文件。

     * @param obj  要序列化的对象
     * @param file 文件路径（包括文件名），将序列化后的对象写入到该路径
     * @throws RuntimeException 如果文件写入失败，则抛出运行时异常
     */
    public static void writeFile(Object obj, String file) {
        Path filePath = Paths.get(file);
        FileUtils.ensureFileDirExist(filePath.toFile());
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(obj);
        } catch (Exception e) {
            throw new RuntimeException("文件生成失败：" + file, e);
        }
    }

    /**
     * 从指定文件中读取对象并反序列化。
     *
     * @param file 文件路径（包括文件名），从该路径读取并反序列化对象
     * @return 反序列化后的对象
     * @throws RuntimeException 如果文件读取失败，则抛出运行时异常
     */
    public static Object readFile(String file) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(file)))) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("文件读取失败：" + file, e);
        }
    }
}

