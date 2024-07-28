package com.github.stazxr.zblog.core.datasource;

import com.github.stazxr.zblog.encryption.Encryptor;
import com.github.stazxr.zblog.encryption.EncryptorContext;
import com.github.stazxr.zblog.util.io.ObjectStreamUtils;
import com.github.stazxr.zblog.util.io.ResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Properties;

/**
 * 扩展自 {@link DriverManagerDataSource}，提供支持加密密码的功能。
 *
 * @author SunTao
 * @since 2024-07-25
 */
@Slf4j
public class SecurityDriverManagerDataSource extends DriverManagerDataSource {
    private String encryptorFile = null;

    private String encryptorKey = null;

    /**
     * 构造函数，根据提供的配置初始化数据源。
     *
     * @param config 包含数据库连接和加密相关配置的 {@link Properties} 对象
     */
    public SecurityDriverManagerDataSource(Properties config) {
        this.setDriverClassName(config.getProperty("zblog.props.db-driver"));
        this.setUrl(config.getProperty("zblog.props.db-url"));
        this.setUsername(config.getProperty("zblog.props.db-user"));
        this.setEncryptorFile(config.getProperty("zblog.props.encryptor-file"));
        this.setEncryptorKey(config.getProperty("zblog.props.encryptor-key"));
        this.setPassword(config.getProperty("zblog.props.db-pass"));
    }

    /**
     * 重写 {@link DriverManagerDataSource#setPassword(String)} 方法，以在设置密码之前进行解密。
     * <p>
     * 如果配置了加密器文件和密钥，将对提供的密码进行解密，然后设置解密后的密码。
     * </p>
     *
     * @param password 加密后的数据库密码
     */
    @Override
    public void setPassword(String password) {
        super.setPassword(decodePassword(password));
    }

    /**
     * 解密密码的方法。
     * <p>
     * 如果设置了加密器文件和密钥，将读取加密器实例并使用它解密密码。
     * </p>
     *
     * @param password 加密的密码
     * @return 解密后的密码
     */
    private String decodePassword(String password) {
        if (encryptorFile != null && !"".equals(encryptorFile) && encryptorKey != null) {
            try {
                EncryptorContext.set(encryptorKey);
                String absolutePath = ResourceLoader.getResourceAbsolutePath(encryptorFile);
                Encryptor encryptor = (Encryptor) ObjectStreamUtils.readFile(absolutePath);
                password = encryptor.decrypt(password);
            } catch (Exception e) {
                log.error("解密数据: {}", password);
                log.error("解密密码失败", e);
            } finally {
                EncryptorContext.remove();
            }
        }

        return password;
    }

    /**
     * 设置加密器文件的路径。
     *
     * @param encryptorFile 加密器文件的路径
     */
    public void setEncryptorFile(String encryptorFile) {
        this.encryptorFile = encryptorFile;
    }

    /**
     * 设置加密器的密钥。
     *
     * @param encryptorKey 加密器的密钥
     */
    public void setEncryptorKey(String encryptorKey) {
        this.encryptorKey = encryptorKey;
    }
}
