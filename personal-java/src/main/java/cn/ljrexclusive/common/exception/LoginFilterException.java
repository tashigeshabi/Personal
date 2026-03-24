package cn.ljrexclusive.common.exception;

import cn.ljrexclusive.common.enums.ResultCode; /**
 * 登录过滤异常
 */
public class LoginFilterException extends BusinessException {
    public LoginFilterException(String message) {
        super(ResultCode.FILTER_FAIL.getCode(), message);
    }

    public LoginFilterException(String code, String message) {
        super(code, message);
    }
}
