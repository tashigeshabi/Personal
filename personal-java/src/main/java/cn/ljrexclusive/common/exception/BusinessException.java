package cn.ljrexclusive.common.exception;


import cn.ljrexclusive.common.enums.ResultCode;
import cn.ljrexclusive.common.service.IResult;
import lombok.Getter;

/**
 * 业务异常类（字符类型状态码）
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String code;

    public BusinessException() {
        super(ResultCode.BUSINESS_ERROR.getMessage());
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(IResult result) {
        super(result.getMessage());
        this.code = result.getCode();
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}

/**
 * 自定义业务异常基类
 */
class BaseBusinessException extends BusinessException {
    public BaseBusinessException(String code, String message) {
        super(code, message);
    }
}

/**
 * 参数校验异常
 */
class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(ResultCode.PARAM_VALIDATE_FAILED.getCode(), message);
    }
}

/**
 * 数据不存在异常
 */
class DataNotFoundException extends BusinessException {
    public DataNotFoundException(String message) {
        super(ResultCode.DATA_NOT_EXIST.getCode(), message);
    }
}

/**
 * 数据已存在异常
 */
class DataAlreadyExistsException extends BusinessException {
    public DataAlreadyExistsException(String message) {
        super(ResultCode.DATA_EXISTED.getCode(), message);
    }
}

/**
 * 认证异常
 */
class AuthenticationException extends BusinessException {
    public AuthenticationException(String message) {
        super(ResultCode.UNAUTHORIZED.getCode(), message);
    }
}

/**
 * 授权异常
 */
class AuthorizationException extends BusinessException {
    public AuthorizationException(String message) {
        super(ResultCode.FORBIDDEN.getCode(), message);
    }
}

