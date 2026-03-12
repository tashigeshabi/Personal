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
    public FilterManager filterManager(List<LoginFilter<?>> filters) {
        FilterManager manager = new FilterManager();

        // 注册所有过滤器
        filters.forEach(manager::registerFilter);

        return manager;
    }

}
