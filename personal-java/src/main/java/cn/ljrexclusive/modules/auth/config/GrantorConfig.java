package cn.ljrexclusive.modules.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * 登录类型配置文件类型
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "login")
public class GrantorConfig {
    private Map<String,String> types;
}
