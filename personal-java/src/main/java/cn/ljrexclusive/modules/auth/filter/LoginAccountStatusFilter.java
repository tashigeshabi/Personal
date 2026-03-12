package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import cn.ljrexclusive.modules.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 *
 * 账户状态过滤器
 */
@Component
@Slf4j
public class LoginAccountStatusFilter implements LoginFilter<LoginRequest> {
    
    @Override
    public String getName() {
        return "loginAccountStatusFilter";
    }
    
    @Override
    public int getOrder() {
        return 400;
    }
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<LoginRequest> context) {
        long startTime = System.currentTimeMillis();
        SysUser sysuser = context.getSysUser();
        
        if (sysuser == null) {
            return LoginFilterResult.fail("USER_NOT_FOUND", "用户不存在");
        }
        
        try {
            if (Boolean.FALSE.equals(sysuser.getStatus())) {
                return LoginFilterResult.fail("USER_ACCOUNT_DISABLE", "账号已禁用");
            }
            if (Boolean.TRUE.equals(sysuser.getDeleted())) {
                return LoginFilterResult.fail("USER_ACCOUNT_NOT_EXIST", "账号不存在");
            }
            
            long duration = System.currentTimeMillis() - startTime;
            LoginFilterResult result = LoginFilterResult.success();
            context.recordExecution(this, result, duration);
            return result;
            
        } catch (Exception e) {
            log.error("账户状态检查异常", e);
            return LoginFilterResult.fail("ACCOUNT_STATUS_ERROR", "账户状态检查异常");
        }
    }
}
