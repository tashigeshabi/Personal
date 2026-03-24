package cn.ljrexclusive.modules.auth.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * access token 密钥
     */
    private String accessKey;
    /**
     * refresh token 密钥
     */
    private String refreshKey;

    /**
     * access token 过期时间（毫秒）
     */
    private Long accessTtl;
    /**
     * refresh token 过期时间（毫秒）
     */
    private Long refreshTtl;

}

