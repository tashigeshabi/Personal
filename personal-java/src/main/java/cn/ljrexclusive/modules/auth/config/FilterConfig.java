package cn.ljrexclusive.modules.auth.config;

import cn.ljrexclusive.modules.auth.filter.manager.FilterManager;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 过滤器配置类
 */
@Configuration
public class FilterConfig {



    @Bean
    public FilterManager filterManager(List<LoginFilter<?>> filters, LoginFilterChainProperties loginFilterChainProperties) {
        FilterManager manager = new FilterManager(loginFilterChainProperties);

        filters.forEach(manager::registerFilter);
        return manager;
    }

}
