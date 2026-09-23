package com.github.stazxr.zblog.bas.props.database;

import com.github.stazxr.zblog.bas.props.datasource.PropsDriverManagerDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义的属性源，从数据库加载配置数据。
 *
 * @author SunTao
 * @since 2024-07-22
 */
public class DatabasePropertySource extends PropertySource<Map<String, Object>> {
    private static final Logger log = LoggerFactory.getLogger(DatabasePropertySource.class);

    /**
     * 配置信息
     */
    Properties config = new Properties();

    /**
     * Spring JDBC Template
     */
    private JdbcTemplate jdbcTemplate;

    private static final String PROPS_FILE = "props-config.properties";

    /**
     * 创建属性源。
     *
     * @param name 属性源名称
     */
    public DatabasePropertySource(String name) {
        super(name, new HashMap<>());
        loadConfigFile();
        initJdbcTemplate();
        initSource();
    }

    /**
     * 加载配置文件。
     *
     * <p>优先从外部配置目录加载，外部配置不存在时从 classpath 加载。
     *
     * <p>生产环境：
     * /appuser/config/props-config.properties
     *
     * <p>开发环境：
     * classpath:/props-config.properties
     */
    private void loadConfigFile() {
        String externalConfigPath = System.getProperty("zblog.config.path");

        try {
            InputStream is = null;

            // 优先加载外部配置文件
            if (externalConfigPath != null && !externalConfigPath.trim().isEmpty()) {
                File file = new File(externalConfigPath, PROPS_FILE);
                if (file.exists() && file.isFile()) {
                    log.info("Loading external config file [{}]", file.getAbsolutePath());
                    is = Files.newInputStream(file.toPath());
                }
            }

            // 外部配置不存在时，从 classpath 加载
            if (is == null) {
                log.info("Loading classpath config file [{}]", PROPS_FILE);
                is = this.getClass().getClassLoader().getResourceAsStream(PROPS_FILE);
            }

            if (is == null) {
                throw new FileNotFoundException("Config file not found: " + PROPS_FILE);
            }

            try (InputStream inputStream = is) {
                config.load(inputStream);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Load config file catch exception[file=" + PROPS_FILE + "]", e);
        }
    }

    private void initJdbcTemplate() {
        jdbcTemplate = new JdbcTemplate(new PropsDriverManagerDataSource(config));
    }

    /**
     * 从数据库加载配置数据，并存储到属性源的 source Map 中。
     */
    private void initSource() {
        this.source.putAll(loadPropertiesFromDatabase());
        log.info("Loading properties from database [{}]", source.size());
    }

    /**
     * 根据属性名称获取属性值。
     *
     * @param k 属性名称
     * @return v 属性值
     */
    @Override
    public Object getProperty(@NonNull String k) {
        return this.source.get(k);
    }

    /**
     * 从数据库加载配置数据的方法。
     *
     * @return 加载的配置数据，以 Map 的形式返回
     */
    private Map<String, Object> loadPropertiesFromDatabase() {
        try {
            Map<String, Object> properties = new HashMap<>(64);
            String enabled = config.getProperty("zblog.props.enabled");
            if (Boolean.TRUE.toString().equals(enabled)) {
                jdbcTemplate.query(config.getProperty("zblog.props.load-sql"), resultSet -> {
                    String name = resultSet.getString("k");
                    String value = resultSet.getString("v");
                    if ("true".equals(value) || "false".equals(value)) {
                        properties.put(name, Boolean.valueOf(value));
                    } else {
                        properties.put(name, value);
                    }
                });
            }
            return properties;
        } catch (Exception e) {
            throw new IllegalStateException("Load properties from database catch exception", e);
        }
    }
}
