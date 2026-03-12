package cn.ljrexclusive.modules.auth.filter;


import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import cn.ljrexclusive.modules.user.entity.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 密码验证过滤器
 */
@Component
@Slf4j
public class LoginPasswordValidationFilter implements LoginFilter<LoginRequest> {
    
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public String getName() {
        return "loginPasswordValidationFilter";
    }
    
    @Override
    public int getOrder() {
        return 300;
    }
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<LoginRequest> context) {
        long startTime = System.currentTimeMillis();
        LoginRequest request = context.getTarget();
        
        // 仅密码登录需要验证密码
        if (!(request instanceof AccountLoginRequest)) {
            return LoginFilterResult.success();
        }
        
        try {

            AccountLoginRequest passwordRequest = (AccountLoginRequest) request;
            SysUser sysuser = context.getSysUser();
            
            if (sysuser == null) {
                return LoginFilterResult.fail("USER_NOT_FOUND", "用户不存在");
            }
            if (!StringUtils.hasText(sysuser.getPassword())) {
                return LoginFilterResult.fail("PASSWORD_NOT_SET", "账号未设置登录密码");
            }

            // TODO 设置错误次数
            // 检查登录失败次数
            // 如果失败次数过多，需要验证码
            if (!passwordEncoder.matches(passwordRequest.getPassword(), sysuser.getPassword())) {
                return LoginFilterResult.fail("PASSWORD_NOT_CORRECT","密码错误");
            }


            // TODO判断账号是否锁定
            // 验证密码，密码错误，记录失败次数
            // 密码正确，清除失败记录
            
            long duration = System.currentTimeMillis() - startTime;
            LoginFilterResult result = LoginFilterResult.success();
            context.recordExecution(this, result, duration);
            
            return result;
            
        } catch (Exception e) {
            log.error("密码验证异常", e);
            return LoginFilterResult.fail("PASSWORD_VALIDATION_ERROR", "密码验证异常");
        }
    }
}
