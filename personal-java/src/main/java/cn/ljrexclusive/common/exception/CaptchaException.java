package cn.ljrexclusive.common.exception;

import cn.ljrexclusive.common.enums.ResultCode; /**
 * 验证码异常
 */
public class CaptchaException extends BusinessException {
    public CaptchaException(String message) {
        super(ResultCode.CAPTCHA_ERROR.getCode(), message);
    }
}
