package cn.ljrexclusive.modules.auth.filter.composite;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * NOT过滤器（取反）
 */
@Slf4j
public class NotFilter<T> implements LoginFilter<T> {
    
    private final LoginFilter<T> wrappedFilter;
    
    public NotFilter(LoginFilter<T> filter) {
        this.wrappedFilter = filter;
    }
    
    @Override
    public String getName() {
        return "not_" + wrappedFilter.getName();
    }
    
    @Override
    public int getOrder() {
        return wrappedFilter.getOrder();
    }
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<T> context) {
        LoginFilterResult result = wrappedFilter.filter(context);
        
        if (result.isPassed()) {
            return LoginFilterResult.fail("FILTER_SHOULD_FAIL",
                    String.format("过滤器 %s 不应该通过", wrappedFilter.getName()));
        } else {
            return LoginFilterResult.success();
        }
    }
}