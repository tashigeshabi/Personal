package cn.ljrexclusive.modules.auth.domain.dto;

import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import cn.ljrexclusive.modules.user.entity.SysUser;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过滤上下文
 */
@Data
@Builder
public class LoginFilterContext<T> {
    
    /**
     * 要过滤的对象
     */
    private T target;
    
    /**
     * 请求信息
     */
    private LoginRequest loginRequest;
    
    /**
     * 用户信息（可能为空）
     */
    private SysUser sysUser;
    
    /**
     * 附加属性
     */
    private Map<String, Object> attributes;
    
    /**
     * 过滤器执行历史
     */
    private List<LoginFilterExecution> executionHistory;
    
    /**
     * 是否中断后续过滤器
     */
    private boolean interrupted;
    
    /**
     * 设置属性
     */
    public void setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
    
    /**
     * 获取属性
     */
    @SuppressWarnings("unchecked")
    public <V> V getAttribute(String key) {
        return attributes != null ? (V) attributes.get(key) : null;
    }
    
    /**
     * 记录过滤器执行
     */
    public void recordExecution(LoginFilter<?> filter, LoginFilterResult result, long duration) {
        if (executionHistory == null) {
            executionHistory = new ArrayList<>();
        }
        executionHistory.add(LoginFilterExecution.builder()
                .filterName(filter.getName())
                .result(result)
                .duration(duration)
                .timestamp(System.currentTimeMillis())
                .build());
    }
}