package cn.ljrexclusive.modules.auth.enums;

import lombok.Getter;

/**
 * 登录类型枚举
 */
@Getter
public enum LoginType {
    
    PASSWORD("account", "账号密码登录"),
    SMS("sms", "短信验证码登录"),
    WECHAT("wechat", "微信登录"),
    EMAIL("email","邮箱登录");
    
    private final String type;
    private final String description;
    
    LoginType(String type, String description) {
        this.type = type;
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据类型字符串获取枚举
     */
    public static LoginType getByType(String type) {
        for (LoginType loginType : values()) {
            if (loginType.getType().equals(type)) {
                return loginType;
            }
        }
        return null;
    }
}