package cn.ljrexclusive.modules.auth.filter.manager;

import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.enums.LoginType;
import cn.ljrexclusive.modules.auth.filter.service.FilterChain;
import cn.ljrexclusive.modules.auth.filter.service.impl.DefaultFilterChain;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 过滤器管理器
 */
@Slf4j
public class FilterManager {

    private static final List<String> ACCOUNT_LOGIN_FILTERS = List.of(
            "loginCaptchaFilter",
            "userLookupByAccountFilter",
            "loginPasswordValidationFilter",
            "loginAccountStatusFilter"
    );
    
    /**
     * 过滤器注册表
     */
    private final Map<String, LoginFilter<?>> filterRegistry = new ConcurrentHashMap<>();
    
    /**
     * 过滤器链缓存
     */
    private final Map<String, FilterChain<?>> chainCache = new ConcurrentHashMap<>();
    
    /**
     * 注册过滤器
     */
    public void registerFilter(LoginFilter<?> filter) {
        String filterName = filter.getName();
        if (filterRegistry.containsKey(filterName)) {
            log.warn("过滤器 {} 已存在，将被覆盖", filterName);
        }
        filterRegistry.put(filterName, filter);
        log.info("注册过滤器: {}", filterName);
    }

    /**
     * 创建账号密码登录AND过滤器链
     */
    public <T extends LoginRequest> FilterChain<T> createAccountLoginFilterChain() {
        return getOrCreateChain(LoginType.PASSWORD, ACCOUNT_LOGIN_FILTERS);
    }

    public <T extends LoginRequest> FilterChain<T> createLoginFilterChain() {
        return createAccountLoginFilterChain();
    }

    @SuppressWarnings("unchecked")
    private <T extends LoginRequest> FilterChain<T> getOrCreateChain(LoginType loginType, List<String> filterNames) {
        String chainKey = "login_" + loginType.getType();
        return (FilterChain<T>) chainCache.computeIfAbsent(chainKey, key -> buildChain(loginType, filterNames));
    }

    @SuppressWarnings("unchecked")
    private <T extends LoginRequest> FilterChain<T> buildChain(LoginType loginType, List<String> filterNames) {
        DefaultFilterChain<T> filterChain = DefaultFilterChain.create();
        for (String filterName : filterNames) {
            LoginFilter<?> filter = filterRegistry.get(filterName);
            if (filter == null) {
                log.warn("登录类型 {} 缺少过滤器: {}", loginType.getType(), filterName);
                continue;
            }
            filterChain.addFilter((LoginFilter<T>) filter);
        }
        if (filterChain.getFilters().isEmpty()) {
            throw new IllegalStateException("未找到可用的登录过滤器，请检查过滤器注册配置");
        }
        log.info("创建登录过滤链: {}, 过滤器数量: {}", loginType.getType(), filterChain.getFilters().size());
        return filterChain;
    }


    /**
     * 动态重新加载过滤器链
     */
    public void reloadFilterChain(LoginType loginType) {
        String chainKey = "login_" + loginType.getType();
        chainCache.remove(chainKey);
        log.info("重新加载过滤器链: {}", loginType);
    }
    
    /**
     * 获取过滤器执行统计
     */
    public Map<String, Object> getFilterStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalFilters", filterRegistry.size());
        stats.put("filterNames", new ArrayList<>(filterRegistry.keySet()));
        stats.put("cachedChains", chainCache.size());
        return stats;
    }
}
