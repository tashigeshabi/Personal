package cn.ljrexclusive.modules.auth.filter.composite;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * OR过滤器（任意子过滤器通过即可）
 */
@Component
@Slf4j
public class OrFilter<T> extends CompositeFilter<T> {
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<T> context) {
        if (filters.isEmpty()) {
            return LoginFilterResult.success();
        }
        
        List<LoginFilterResult> failedResults = new ArrayList<>();
        Map<String, Object> mergedExtraData = new HashMap<>();
        
        for (LoginFilter<T> filter : filters) {
            LoginFilterResult result = filter.filter(context);
            
            if (result.isPassed()) {
                // 任意一个成功即返回成功
                if (result.getExtraData() != null) {
                    mergedExtraData.putAll(result.getExtraData());
                }
                return LoginFilterResult.success(mergedExtraData);
            }
            
            failedResults.add(result);
        }
        
        // 所有过滤器都失败
        if (failedResults.size() == 1) {
            return failedResults.get(0);
        }
        
        // 组合错误信息
        String errorMessage = failedResults.stream()
                .map(LoginFilterResult::getErrorMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("; "));
        
        return LoginFilterResult.fail("ALL_FILTERS_FAILED", errorMessage);
    }
}
