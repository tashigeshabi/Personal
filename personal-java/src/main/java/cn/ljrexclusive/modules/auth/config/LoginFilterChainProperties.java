package cn.ljrexclusive.modules.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录过滤链配置映射。
 *
 * <p>对应配置示例：</p>
 * <pre>
 * filter:
 *   chains:
 *     login:
 *       account:
 *         enabled: true
 *         filters:
 *           - loginCaptchaFilter
 *           - userLookupByAccountFilter
 * </pre>
 */
@Data
@Component
@ConfigurationProperties(prefix = "filter.chains")
public class LoginFilterChainProperties {
    /**
     * key 为登录类型（account/sms/wechat），value 为该类型链路配置。
     */
    private Map<String, ChainConfig> login = new LinkedHashMap<>();

    @Data
    public static class ChainConfig {
        /**
         * 是否启用该登录类型链路。
         */
        private boolean enabled = true;

        /**
         * 按顺序执行的过滤器 Bean 名称列表。
         */
        private List<String> filters = new ArrayList<>();
    }
}
