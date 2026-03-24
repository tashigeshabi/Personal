package cn.ljrexclusive.modules.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录安全策略配置。
 *
 * <p>用于控制密码登录失败后的风控行为。</p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "login.security")
public class LoginSecurityProperties {
    /**
     * 触发账号临时锁定所需的连续失败次数。
     */
    private int maxFailedAttempts = 5;

    /**
     * 账号锁定时长（秒）。
     */
    private long lockDurationSeconds = 300;
}
