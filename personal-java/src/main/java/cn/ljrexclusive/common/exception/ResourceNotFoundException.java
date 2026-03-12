package cn.ljrexclusive.common.exception;

import cn.ljrexclusive.common.enums.ResultCode; /**
 * 获取用户异常
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(ResultCode.USER_REQUIRE_FAIL.getCode(), message);
    }
}
