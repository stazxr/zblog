package com.github.stazxr.zblog.audit.provider.source;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件词库
 *
 * @author SunTao
 * @since 2026-07-05
 */
@Component
public class FileSensitiveWordSource implements SensitiveWordSource {
    private static final String FILE_PATH = "sensitive-word.txt";

    @Override
    public List<String> load() {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new ClassPathResource(FILE_PATH).getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("加载文件敏感词失败", e);
        }
        return words;
    }
}
