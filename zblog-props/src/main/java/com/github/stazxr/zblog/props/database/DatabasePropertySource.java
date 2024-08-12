package com.github.stazxr.zblog.props.database;

import com.github.stazxr.zblog.props.PropertySourceLoadException;
import com.github.stazxr.zblog.props.datasource.PropsDriverManagerDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义的属性源，从数据库加载配置数据。
 *
 * @author SunTao
 * @since 2024-07-22
 */
@Slf4j
public class DatabasePropertySource extends PropertySource<Map<String, String>> {
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

    private void loadConfigFile() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPS_FILE)) {
            config.load(is);
        } catch (Exception e) {
            throw new PropertySourceLoadException("Load config file catch exception[file=" + PROPS_FILE + "]", e);
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
        log.info("Load properties size: {}", source.size());
    }

    /**
     * 根据属性名称获取属性值。
     *
     * @param k 属性名称
     * @return v 属性值
     */
    @Override
    public Object getProperty(@NotNull String k) {
        return this.source.get(k);
    }

    /**
     * 从数据库加载配置数据的方法。
     *
     * @return 加载的配置数据，以 Map 的形式返回
     */
    private Map<String, String> loadPropertiesFromDatabase() {
        try {
            Map<String, String> properties = new HashMap<>(64);
            String enabled = config.getProperty("zblog.props.enabled");
            if (Boolean.TRUE.toString().equals(enabled)) {
                log.info("Loading properties from database.");
                jdbcTemplate.query(config.getProperty("zblog.props.load-sql"), resultSet -> {
                    String name = resultSet.getString("k");
                    String value = resultSet.getString("v");
                    properties.put(name, value);
                });
            }
            return properties;
        } catch (Exception e) {
            throw new PropertySourceLoadException("Load properties from database catch exception", e);
        }
    }
}
