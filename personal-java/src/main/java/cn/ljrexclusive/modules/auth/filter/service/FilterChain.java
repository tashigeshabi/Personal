package cn.ljrexclusive.modules.auth.filter.service;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.service.LoginFilter;

import java.util.List;


/**
 * 过滤器链接口
 */
public interface FilterChain<T> {
    
    /**
     * 执行过滤链
     * @param context 过滤上下文
     * @return 最终过滤结果
     */
    LoginFilterResult doFilter(LoginFilterContext<T> context);
    
    /**
     * 添加过滤器
     */
    void addFilter(LoginFilter<T> filter);
    
    /**
     * 移除过滤器
     */
    void removeFilter(LoginFilter<T> filter);
    
    /**
     * 获取所有过滤器
     */
    List<LoginFilter<T>> getFilters();
}
