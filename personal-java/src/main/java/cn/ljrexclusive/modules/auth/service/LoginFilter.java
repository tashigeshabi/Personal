package cn.ljrexclusive.modules.auth.service;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;

/**
 * 登录过滤器接口
 * @param <T> 过滤的对象类型
 */
public interface LoginFilter<T> {
    
    /**
     * 执行过滤
     * @param context 过滤上下文
     * @return 过滤结果
     */
    LoginFilterResult filter(LoginFilterContext<T> context);
    
    /**
     * 过滤器名称
     */
    String getName();
    
    /**
     * 执行顺序（越小越先执行）
     */
    int getOrder();
}