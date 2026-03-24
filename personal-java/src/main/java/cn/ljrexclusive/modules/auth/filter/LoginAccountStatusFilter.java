package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.enums.LoginFilterErrorCode;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 *
 * 账户状态过滤器
 */
@Component
@Slf4j
public class LoginAccountStatusFilter extends AbstractLoginFilter<LoginRequest> {

    public LoginAccountStatusFilter(LoginFilterMonitorService loginFilterMonitorService) {
        super(loginFilterMonitorService);
    }
    
    @Override
    public String getName() {
        return "loginAccountStatusFilter";
    }
    
    @Override
    public int getOrder() {
        return 400;
    }
    
    @Override
    protected LoginFilterResult doFilterInternal(LoginFilterContext<LoginRequest> context) {
        SysUser sysuser = context.getSysUser();
        if (sysuser == null) {
            return LoginFilterResult.fail(LoginFilterErrorCode.USER_NOT_FOUND.getCode(), LoginFilterErrorCode.USER_NOT_FOUND.getMessage());
        }

        try {
            if (Boolean.FALSE.equals(sysuser.getStatus())) {
                return LoginFilterResult.fail(LoginFilterErrorCode.USER_ACCOUNT_DISABLE.getCode(), LoginFilterErrorCode.USER_ACCOUNT_DISABLE.getMessage());
            }
            if (Boolean.TRUE.equals(sysuser.getDeleted())) {
                return LoginFilterResult.fail(LoginFilterErrorCode.USER_ACCOUNT_NOT_EXIST.getCode(), LoginFilterErrorCode.USER_ACCOUNT_NOT_EXIST.getMessage());
            }
            return LoginFilterResult.success();
        } catch (Exception e) {
            log.error("账户状态检查异常", e);
            return LoginFilterResult.fail(LoginFilterErrorCode.ACCOUNT_STATUS_ERROR.getCode(), LoginFilterErrorCode.ACCOUNT_STATUS_ERROR.getMessage());
        }
    }
}
