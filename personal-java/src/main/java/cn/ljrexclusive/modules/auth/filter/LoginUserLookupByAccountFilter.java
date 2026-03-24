package cn.ljrexclusive.modules.auth.filter;


import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.enums.LoginFilterErrorCode;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.enums.LoginType;
import cn.ljrexclusive.modules.user.entity.SysUser;
import cn.ljrexclusive.modules.user.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户查找过滤器(根据用户名)
 */


@Component
@Slf4j
public class LoginUserLookupByAccountFilter extends AbstractLoginFilter<LoginRequest> {

    private final ISysUserService sysUserService;

    public LoginUserLookupByAccountFilter(ISysUserService sysUserService, LoginFilterMonitorService loginFilterMonitorService) {
        super(loginFilterMonitorService);
        this.sysUserService = sysUserService;
    }

    
    @Override
    public String getName() {
        return "userLookupByAccountFilter";
    }
    
    @Override
    public int getOrder() {
        return 200;
    }
    
    @Override
    protected LoginFilterResult doFilterInternal(LoginFilterContext<LoginRequest> context) {
        LoginRequest request = context.getTarget();
        if (!(request instanceof AccountLoginRequest)) {
            return LoginFilterResult.success();
        }
        if (!LoginType.PASSWORD.getType().equals(request.getLoginType())) {
            return LoginFilterResult.success();
        }
        try {
            Map<String, Object> extraData = new HashMap<>();
            String username = ((AccountLoginRequest) request).getUsername();
            SysUser sysuser = sysUserService.findByUsername(username);
            if (sysuser == null) {
                return LoginFilterResult.fail(LoginFilterErrorCode.USER_NOT_FOUND.getCode(), LoginFilterErrorCode.USER_NOT_FOUND.getMessage());
            }
            extraData.put("userId", sysuser.getId());
            context.setSysUser(sysuser);
            return LoginFilterResult.success(extraData);
        } catch (Exception e) {
            log.error("用户查找异常", e);
            return LoginFilterResult.fail(LoginFilterErrorCode.USER_LOOKUP_ERROR.getCode(), LoginFilterErrorCode.USER_LOOKUP_ERROR.getMessage());
        }
    }

}
