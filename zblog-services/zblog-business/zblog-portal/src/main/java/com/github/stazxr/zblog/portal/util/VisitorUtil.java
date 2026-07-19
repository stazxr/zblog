package com.github.stazxr.zblog.portal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 游客工具
 *
 * @author SunTao
 * @since 2026-07-19
 */
public final class VisitorUtil {
    private static final Logger log = LoggerFactory.getLogger(VisitorUtil.class);

    private static final List<String> NICKNAME_POOL = new ArrayList<>();

    private static final String AVATAR_PATH = "/avatar/visitor/%02d.png";

    private static final String NICKNAME_FILE = "visitor/visitorNickname.txt";

    static {
        try (InputStream is = VisitorUtil.class.getClassLoader().getResourceAsStream(NICKNAME_FILE)) {
            if (is == null) {
                throw new IllegalStateException("visitor nickname file not found:" + NICKNAME_FILE);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        NICKNAME_POOL.add(line.trim());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("load visitor nickname error: {}", e.getMessage());
            NICKNAME_POOL.clear();
            NICKNAME_POOL.add("游客");
        }
    }

    private VisitorUtil() {
    }

    /**
     * 生成游客昵称
     *
     * @param visitorId 游客id
     */
    public static String getNickname(String visitorId) {
        int index = Math.abs(visitorId.hashCode()) % NICKNAME_POOL.size();
        String prefix = NICKNAME_POOL.get(index);
        String suffix = visitorId.substring(visitorId.length() - 4).toUpperCase();
        return prefix + suffix;
    }

    /**
     * 获取游客头像
     *
     * @param visitorId 游客id
     */
    public static String getAvatar(String visitorId) {
        int index = Math.abs(visitorId.hashCode()) % 30 + 1;
        return String.format(AVATAR_PATH, index);
    }
}
