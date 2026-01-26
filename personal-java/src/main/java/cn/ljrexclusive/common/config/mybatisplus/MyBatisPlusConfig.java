package cn.ljrexclusive.common.config.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.ljrexclusive.modules.*.mapper")
public class MyBatisPlusConfig {
    
    /**
     * 添加分页插件
     */
}