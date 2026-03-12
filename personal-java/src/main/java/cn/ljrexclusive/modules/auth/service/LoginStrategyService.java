package cn.ljrexclusive.modules.auth.service;

import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.domain.res.UserLoginResponse;

/**
 * 登录策略接口
 * 使用泛型 T 指定具体的登录参数类型
 */
public interface LoginStrategyService<T extends LoginRequest> {

    /**
     * 登录
     * @param request 登录请求参数
     * @return 登录结果
     */
    UserLoginResponse login(T request);
    
    /**
     * 登录前预处理
     */
    default void preLogin(T request) {
        request.validate();
    }
    
    /**
     * 登录后处理
     */
    default void postLogin(T request, UserLoginResponse result) {
        // 可以记录登录日志、发送事件等
    }

}