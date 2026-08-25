package com.github.stazxr.zblog.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * UrlUtils
 *
 * @author SunTao
 * @since 2026-08-25
 */
public final class UrlUtils {
    private static final Logger log = LoggerFactory.getLogger(UrlUtils.class);

    private UrlUtils() {
    }

    public static String normalize(String url) {
        if (url == null || url.trim().isEmpty()) {
            return url;
        }
        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme();
            String host = uri.getHost();
            if (scheme == null || host == null) {
                throw new IllegalArgumentException("非法URL：" + url);
            }
            scheme = scheme.toLowerCase();
            host = host.toLowerCase();
            int port = uri.getPort();
            if (("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
                port = -1;
            }
            String path = uri.getRawPath();
            if ("/".equals(path)) {
                path = null;
            }
            return new URI(
                scheme, uri.getRawUserInfo(), host, port, path, uri.getRawQuery(), null
            ).toASCIIString();
        } catch (URISyntaxException e) {
            log.error("非法URL：{}", url, e);
            return url;
        }
    }
}
