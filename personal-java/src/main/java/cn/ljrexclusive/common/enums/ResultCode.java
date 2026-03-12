package cn.ljrexclusive.common.enums;


import cn.ljrexclusive.common.service.IResult;

/**
 * 字符类型状态码枚举
 */
public enum ResultCode implements IResult {
    // 成功状态码
    SUCCESS("200", "操作成功"),
    
    // 客户端错误状态码
    BAD_REQUEST("400", "请求参数错误"),
    UNAUTHORIZED("401", "未授权"),
    FORBIDDEN("403", "禁止访问"),
    NOT_FOUND("404", "资源不存在"),
    METHOD_NOT_ALLOWED("405", "请求方法不允许"),
    
    // 服务器错误状态码
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    SERVICE_UNAVAILABLE("503", "服务不可用"),
    
    // 业务错误状态码 - 使用字母前缀提高可读性
    BUSINESS_ERROR("B0001", "业务异常"),
    DATA_NOT_EXIST("B0002", "数据不存在"),
    DATA_EXISTED("B0003", "数据已存在"),
    PARAM_VALIDATE_FAILED("B0004", "参数校验失败"),
    UPLOAD_FAILED("B0005", "文件上传失败"),
    DOWNLOAD_FAILED("B0006", "文件下载失败"),
    CAPTCHA_ERROR("B0007", "验证码错误"),
    CAPTCHA_EXPIRED("B0008", "验证码已过期"),
    OPERATION_TOO_FREQUENT("B0009", "操作过于频繁，请稍后再试"),
    
    // 用户相关错误
    USER_NOT_LOGIN("U0001", "用户未登录"),
    USER_LOGIN_ERROR("U0002", "账号或密码错误"),
    USER_ACCOUNT_EXPIRED("U0003", "账号已过期"),
    USER_CREDENTIALS_ERROR("U0004", "密码错误"),
    USER_CREDENTIALS_EXPIRED("U0005", "密码过期"),
    USER_ACCOUNT_DISABLE("U0006", "账号不可用"),
    USER_ACCOUNT_LOCKED("U0007", "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST("U0008", "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST("U0009", "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS("U0010", "账号下线"),
    USER_TOKEN_INVALID("U0011", "Token无效"),
    USER_TOKEN_EXPIRED("U0012", "Token已过期"),
    USER_REQUIRE_FAIL("U0013", "获取用户错误"),

    // 过滤连相关错误
    FILTER_FAIL("F0001","登录过滤异常"),
    
    // 权限相关错误
    PERMISSION_NO_ACCESS("P0001", "无访问权限"),
    PERMISSION_DENIED("P0002", "权限不足"),
    
    // 数据相关错误
    DATA_VALIDATE_ERROR("D0001", "数据校验失败"),
    DATA_FORMAT_ERROR("D0002", "数据格式错误"),
    DATA_INTEGRITY_ERROR("D0003", "数据完整性错误"),
    
    // 系统配置错误
    CONFIG_ERROR("C0001", "系统配置错误"),
    
    // 外部服务错误
    THIRD_PARTY_ERROR("E0001", "第三方服务异常"),
    API_CALL_FAILED("E0002", "接口调用失败"),
    NETWORK_ERROR("E0003", "网络异常");

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 根据状态码获取枚举实例
     */
    public static ResultCode getByCode(String code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }

    /**
     * 判断是否为成功状态码
     */
    public static boolean isSuccess(String code) {
        return SUCCESS.getCode().equals(code);
    }

    /**
     * 判断是否为客户端错误状态码
     */
    public static boolean isClientError(String code) {
        return code.startsWith("4") || code.startsWith("U") || code.startsWith("P");
    }

    /**
     * 判断是否为服务器错误状态码
     */
    public static boolean isServerError(String code) {
        return code.startsWith("5") || code.startsWith("B") || 
               code.startsWith("D") || code.startsWith("E");
    }
}