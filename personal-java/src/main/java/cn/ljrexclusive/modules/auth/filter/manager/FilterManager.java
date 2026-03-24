package cn.ljrexclusive.modules.auth.filter.manager;

import cn.ljrexclusive.modules.auth.config.LoginFilterChainProperties;
import cn.ljrexclusive.modules.auth.enums.LoginFilterErrorCode;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.enums.LoginType;
import cn.ljrexclusive.modules.auth.filter.service.FilterChain;
import cn.ljrexclusive.modules.auth.filter.service.impl.DefaultFilterChain;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录过滤器管理器。
 *
 * <p>核心职责：</p>
 * <ul>
 *   <li>注册过滤器 Bean（name -> filter 实例）。</li>
 *   <li>根据登录类型解析应执行的过滤器列表。</li>
 *   <li>构建并缓存过滤器链，减少重复构建开销。</li>
 *   <li>对配置错误进行快速失败，避免无声降级。</li>
 * </ul>
 */
@Slf4j
public class FilterManager {

    /**
     * 从 YAML 读取的登录过滤链配置。
     */
    private final LoginFilterChainProperties loginFilterChainProperties;

    /**
     * 默认过滤链（当外部配置缺失时的兜底策略）。
     */
    private final Map<String, List<String>> defaultLoginChains = new HashMap<>();

    /**
     * 已注册过滤器。
     */
    private final Map<String, LoginFilter<?>> filterRegistry = new ConcurrentHashMap<>();

    /**
     * 过滤器链缓存，key 形如 login_account/login_sms/login_wechat。
     */
    private final Map<String, FilterChain<?>> chainCache = new ConcurrentHashMap<>();

    public FilterManager(LoginFilterChainProperties loginFilterChainProperties) {
        this.loginFilterChainProperties = loginFilterChainProperties;
        defaultLoginChains.put(LoginType.PASSWORD.getType(), List.of("loginCaptchaFilter", "userLookupByAccountFilter", "loginPasswordValidationFilter", "loginAccountStatusFilter"));
        defaultLoginChains.put(LoginType.SMS.getType(), List.of("loginCaptchaFilter", "userLookupByAccountFilter", "loginAccountStatusFilter"));
        defaultLoginChains.put(LoginType.WECHAT.getType(), List.of("userLookupByAccountFilter", "loginAccountStatusFilter"));
    }

    public void registerFilter(LoginFilter<?> filter) {
        String filterName = filter.getName();
        if (filterRegistry.containsKey(filterName)) {
            log.warn("过滤器 {} 已存在，将被覆盖", filterName);
        }
        filterRegistry.put(filterName, filter);
        log.info("注册过滤器: {}", filterName);
    }

    public <T extends LoginRequest> FilterChain<T> createAccountLoginFilterChain() {
        return createLoginFilterChain(LoginType.PASSWORD.getType());
    }

    public <T extends LoginRequest> FilterChain<T> createSmsLoginFilterChain() {
        return createLoginFilterChain(LoginType.SMS.getType());
    }

    public <T extends LoginRequest> FilterChain<T> createWechatLoginFilterChain() {
        return createLoginFilterChain(LoginType.WECHAT.getType());
    }

    public <T extends LoginRequest> FilterChain<T> createLoginFilterChain() {
        return createAccountLoginFilterChain();
    }

    public <T extends LoginRequest> FilterChain<T> createLoginFilterChain(String loginType) {
        LoginType type = LoginType.getByType(loginType);
        if (type == null) {
            throw new IllegalStateException("不支持的登录类型: " + loginType);
        }
        // 先解析“要跑哪些过滤器”，再构建/复用链
        List<String> filterNames = resolveFilterNames(type);
        return getOrCreateChain(type, filterNames);
    }

    @SuppressWarnings("unchecked")
    private <T extends LoginRequest> FilterChain<T> getOrCreateChain(LoginType loginType, List<String> filterNames) {
        String chainKey = "login_" + loginType.getType();
        // computeIfAbsent 保证同一 key 只会构建一次
        return (FilterChain<T>) chainCache.computeIfAbsent(chainKey, key -> buildChain(loginType, filterNames));
    }

    /**
     * 解析过滤器名称列表。
     *
     * <p>优先级：</p>
     * <ol>
     *   <li>使用 YAML 中对应登录类型配置。</li>
     *   <li>如果未配置或空配置，回退到默认链。</li>
     * </ol>
     */
    private List<String> resolveFilterNames(LoginType loginType) {
        LoginFilterChainProperties.ChainConfig chainConfig = loginFilterChainProperties.getLogin().get(loginType.getType());
        if (chainConfig != null) {
            if (!chainConfig.isEnabled()) {
                throw new IllegalStateException("登录类型已禁用: " + loginType.getType());
            }
            if (chainConfig.getFilters() != null && !chainConfig.getFilters().isEmpty()) {
                return chainConfig.getFilters();
            }
        }
        return defaultLoginChains.getOrDefault(loginType.getType(), Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    private <T extends LoginRequest> FilterChain<T> buildChain(LoginType loginType, List<String> filterNames) {
        DefaultFilterChain<T> filterChain = DefaultFilterChain.create();
        for (String filterName : filterNames) {
            LoginFilter<?> filter = filterRegistry.get(filterName);
            if (filter == null) {
                // 配置中出现不存在的 Bean 名称，直接报错阻断启动链路
                throw new IllegalStateException(LoginFilterErrorCode.FILTER_CONFIG_ERROR.getCode() + ":" + loginType.getType() + " 缺少过滤器 " + filterName);
            }
            filterChain.addFilter((LoginFilter<T>) filter);
        }
        if (filterChain.getFilters().isEmpty()) {
            throw new IllegalStateException(LoginFilterErrorCode.FILTER_CONFIG_ERROR.getCode() + ":未找到可用的登录过滤器");
        }
        log.info("创建登录过滤链: {}, 过滤器数量: {}", loginType.getType(), filterChain.getFilters().size());
        return filterChain;
    }


    public void reloadFilterChain(LoginType loginType) {
        String chainKey = "login_" + loginType.getType();
        // 删除缓存，下一次请求会按最新配置重建
        chainCache.remove(chainKey);
        log.info("重新加载过滤器链: {}", loginType);
    }

    public Map<String, Object> getFilterStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFilters", filterRegistry.size());
        stats.put("filterNames", new ArrayList<>(filterRegistry.keySet()));
        stats.put("cachedChains", chainCache.size());
        return stats;
    }
}
