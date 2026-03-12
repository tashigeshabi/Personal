package cn.ljrexclusive.modules.auth.filter.composite;

import cn.ljrexclusive.modules.auth.service.LoginFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 复合过滤器基类
 */
public abstract class CompositeFilter<T> implements LoginFilter<T> {
    
    protected final List<LoginFilter<T>> filters = new ArrayList<>();
    
    /**
     * 添加子过滤器
     */
    public void addFilter(LoginFilter<T> filter) {
        filters.add(filter);
    }
    
    /**
     * 移除子过滤器
     */
    public void removeFilter(LoginFilter<T> filter) {
        filters.remove(filter);
    }
    
    /**
     * 获取子过滤器数量
     */
    public int getFilterCount() {
        return filters.size();
    }
    
    /**
     * 清空子过滤器
     */
    public void clearFilters() {
        filters.clear();
    }
    
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
    
    @Override
    public int getOrder() {
        return 0; // 复合过滤器通常放在链的开始或结束
    }
}