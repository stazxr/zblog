package com.github.stazxr.zblog.bas.file;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileUploadProgress {
    private static final Map<Long, Integer> uploadProgress = new ConcurrentHashMap<>();

    public static void put(Long fileId, Integer process) {
        uploadProgress.put(fileId, process);
    }

    public static Integer get(Long fileId) {
        return uploadProgress.getOrDefault(fileId, 0);
    }

    public static void remove(Long fileId) {
        uploadProgress.remove(fileId);
    }
}
