package cn.ljrexclusive.modules.auth.filter.composite;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AND过滤器（所有子过滤器都必须通过）
 */
@Component
@Slf4j
public class AndFilter<T> extends CompositeFilter<T> {
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<T> context) {
        if (filters.isEmpty()) {
            return LoginFilterResult.success();
        }
        
        Map<String, Object> mergedExtraData = new HashMap<>();
        
        for (LoginFilter<T> filter : filters) {
            LoginFilterResult result = filter.filter(context);
            
            if (!result.isPassed()) {
                log.debug("AND过滤器子过滤器 {} 失败", filter.getName());
                return result;
            }
            
            // 合并额外数据
            if (result.getExtraData() != null) {
                mergedExtraData.putAll(result.getExtraData());
            }
        }

        return LoginFilterResult.success(mergedExtraData);
    }
}
