package cn.ljrexclusive.modules.auth.filter.service.impl;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.filter.service.FilterChain;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认过滤器链实现
 */
@Component
@Slf4j
public class DefaultFilterChain<T> implements FilterChain<T> {
    
    private final List<LoginFilter<T>> filters = new CopyOnWriteArrayList<>();
    private final AtomicInteger index = new AtomicInteger(0);
    
    /**
     * 静态工厂方法
     */
    public static <T> DefaultFilterChain<T> create() {
        return new DefaultFilterChain<>();
    }
    
    /**
     * 执行过滤链
     */
    @Override
    public LoginFilterResult doFilter(LoginFilterContext<T> context) {
        // 如果过滤器为空，直接返回成功
        if (filters.isEmpty()) {
            return LoginFilterResult.success();
        }
        
        // 重置索引（为了支持链的重用）
        index.set(0);
        
        return internalDoFilter(context);
    }
    
    /**
     * 内部递归执行过滤器
     */
    private LoginFilterResult internalDoFilter(LoginFilterContext<T> context) {
        int currentIndex = index.getAndIncrement();
        
        // 已执行完所有过滤器
        if (currentIndex >= filters.size()) {
            return LoginFilterResult.success();
        }
        
        LoginFilter<T> currentFilter = filters.get(currentIndex);
        
        try {
            log.debug("执行过滤器: {} (order: {})", 
                    currentFilter.getName(), currentFilter.getOrder());
            
            // 执行当前过滤器
            LoginFilterResult result = currentFilter.filter(context);
            
            // 如果过滤器失败，中断链
            if (!result.isPassed()) {
                log.debug("过滤器 {} 执行失败: {}", 
                        currentFilter.getName(), result.getErrorMessage());
                
                // 设置中断标志
                context.setInterrupted(true);
                return result;
            }
            
            // 合并额外数据到上下文
            if (result.getExtraData() != null) {
                result.getExtraData().forEach(context::setAttribute);
            }
            
            // 继续执行下一个过滤器
            return internalDoFilter(context);
            
        } catch (Exception e) {
            log.error("过滤器 {} 执行异常", currentFilter.getName(), e);
            context.setInterrupted(true);
            return LoginFilterResult.fail("FILTER_EXCEPTION",
                    String.format("过滤器 %s 执行异常: %s", 
                            currentFilter.getName(), e.getMessage()));
        }
    }
    
    @Override
    public void addFilter(LoginFilter<T> filter) {
        filters.add(filter);
        // 按order排序
        filters.sort(Comparator.comparingInt(LoginFilter::getOrder));
    }
    
    @Override
    public void removeFilter(LoginFilter<T> filter) {
        filters.remove(filter);
    }
    
    @Override
    public List<LoginFilter<T>> getFilters() {
        return Collections.unmodifiableList(filters);
    }
    
    /**
     * 批量添加过滤器
     */
    public void addFilters(List<LoginFilter<T>> filters) {
        this.filters.addAll(filters);
        this.filters.sort(Comparator.comparingInt(LoginFilter::getOrder));
    }
    
    /**
     * 清空过滤器
     */
    public void clearFilters() {
        filters.clear();
    }
}