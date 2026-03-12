package cn.ljrexclusive.modules.auth.domain.req.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录参数基类
 */
@Data
public abstract class LoginRequest implements Serializable {
    
    /**
     * 登录类型
     */
    private String loginType;
    
    /**
     * 客户端ID（用于OAuth2）
     */
    private String clientId = "default";
    
    /**
     * 设备信息
     */
    private String deviceId;
    
    /**
     * IP地址
     */
    private String ip;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 验证登录参数
     */
    public abstract void validate();
}