package cn.ljrexclusive.modules.auth.filter;


import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.enums.LoginType;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import cn.ljrexclusive.modules.user.entity.SysUser;
import cn.ljrexclusive.modules.user.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户查找过滤器(根据用户名)
 */


@Component
@Slf4j
public class LoginUserLookupByAccountFilter implements LoginFilter<LoginRequest> {
    
    @Resource
    private ISysUserService sysUserService;

    
    @Override
    public String getName() {
        return "userLookupByAccountFilter";
    }
    
    @Override
    public int getOrder() {
        return 200;
    }
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<LoginRequest> context) {
        long startTime = System.currentTimeMillis();
        LoginRequest request = context.getTarget();
        
        try {
            if (!(request instanceof AccountLoginRequest)) {
                return LoginFilterResult.success();
            }
            if (!LoginType.PASSWORD.getType().equals(request.getLoginType())) {
                return LoginFilterResult.success();
            }
            Map<String, Object> extraData = new HashMap<>();
            String username = ((AccountLoginRequest) request).getUsername();
            SysUser sysuser = sysUserService.findByUsername(username);
            if (sysuser == null) {
                return LoginFilterResult.fail("USER_NOT_FOUND", "用户不存在");
            }
            extraData.put("userId", sysuser.getId());
            // 设置用户到上下文
            context.setSysUser(sysuser);
            
            long duration = System.currentTimeMillis() - startTime;
            LoginFilterResult result = LoginFilterResult.success(extraData);
            context.recordExecution(this, result, duration);
            
            return result;
            
        } catch (Exception e) {
            log.error("用户查找异常", e);
            return LoginFilterResult.fail("USER_LOOKUP_ERROR", "用户查找异常");
        }
    }

}
