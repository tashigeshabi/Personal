package cn.ljrexclusive.modules.auth.enums;

import lombok.Getter;

/**
 * 登录过滤器统一错误码。
 *
 * <p>编码约定：</p>
 * <ul>
 *   <li>F1xxx：验证码相关</li>
 *   <li>F2xxx：用户查询相关</li>
 *   <li>F3xxx：密码与风控相关</li>
 *   <li>F4xxx：账号状态相关</li>
 *   <li>F9xxx：框架与配置相关</li>
 * </ul>
 */
@Getter
public enum LoginFilterErrorCode {
    /**
     * 验证码标识缺失，无法定位要校验的验证码。
     */
    CAPTCHA_KEY_MISSING("F1001", "验证码标识不能为空"),
    /**
     * 验证码输入错误。
     */
    CAPTCHA_INVALID("F1002", "验证码错误"),
    /**
     * 验证码服务发生系统异常。
     */
    CAPTCHA_SYSTEM_ERROR("F1003", "验证码系统异常"),
    /**
     * 用户不存在。
     */
    USER_NOT_FOUND("F2001", "用户不存在"),
    /**
     * 用户查询过程发生异常。
     */
    USER_LOOKUP_ERROR("F2002", "用户查找异常"),
    /**
     * 账号未设置密码。
     */
    PASSWORD_NOT_SET("F3001", "账号未设置登录密码"),
    /**
     * 密码错误。
     */
    PASSWORD_NOT_CORRECT("F3002", "密码错误"),
    /**
     * 触发登录失败次数阈值后进入锁定状态。
     */
    ACCOUNT_LOCKED("F3003", "账号已锁定，请稍后再试"),
    /**
     * 密码校验执行异常。
     */
    PASSWORD_VALIDATION_ERROR("F3004", "密码验证异常"),
    /**
     * 账号被禁用。
     */
    USER_ACCOUNT_DISABLE("F4001", "账号已禁用"),
    /**
     * 账号被删除或逻辑不存在。
     */
    USER_ACCOUNT_NOT_EXIST("F4002", "账号不存在"),
    /**
     * 账号状态检查异常。
     */
    ACCOUNT_STATUS_ERROR("F4003", "账户状态检查异常"),
    /**
     * 过滤器执行时出现未预期异常。
     */
    FILTER_EXCEPTION("F9001", "过滤器执行异常"),
    /**
     * 过滤器链配置不合法（如配置了不存在的过滤器 Bean）。
     */
    FILTER_CONFIG_ERROR("F9002", "过滤器链配置错误");

    /**
     * 错误码。
     */
    private final String code;

    /**
     * 对外错误消息。
     */
    private final String message;

    LoginFilterErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
