package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.common.exception.CaptchaException;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.service.CaptchaService;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 验证码过滤器
 */

@Component
@Slf4j
public class LoginCaptchaFilter implements LoginFilter<LoginRequest> {
    
    @Resource
    private CaptchaService captchaService;
    
    @Override
    public String getName() {
        return "loginCaptchaFilter";
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
    
    @Override
    public LoginFilterResult filter(LoginFilterContext<LoginRequest> context) {
        long startTime = System.currentTimeMillis();
        LoginRequest request = context.getTarget();
        
        try {
            // 只验证密码登录的验证码
            if (!(request instanceof AccountLoginRequest)) {
                log.debug("跳过验证码验证，登录类型: {}", request.getLoginType());
                return LoginFilterResult.success();
            }

            AccountLoginRequest passwordRequest = (AccountLoginRequest) request;

            // 如果有验证码则验证，没有则跳过
            if (StringUtils.hasText(passwordRequest.getCaptcha())) {
                if (!StringUtils.hasText(passwordRequest.getCaptchaKey())) {
                    return LoginFilterResult.fail("CAPTCHA_KEY_MISSING", "验证码标识不能为空");
                }
                
                captchaService.verify(passwordRequest.getCaptchaKey(), passwordRequest.getCaptcha());
                log.debug("验证码验证成功");
                
                // 设置验证通过标记
                context.setAttribute("captchaVerified", true);
            } else {
                log.debug("未提供验证码，跳过验证");
                context.setAttribute("captchaVerified", false);
            }
            
            long duration = System.currentTimeMillis() - startTime;
            LoginFilterResult result = LoginFilterResult.success();
            context.recordExecution(this, result, duration);
            
            return result;
            
        } catch (CaptchaException e) {
            log.warn("验证码验证失败: {}", e.getMessage());
            return LoginFilterResult.fail("CAPTCHA_INVALID", "验证码错误");
        } catch (Exception e) {
            log.error("验证码验证异常", e);
            return LoginFilterResult.fail("CAPTCHA_SYSTEM_ERROR", "验证码系统异常");
        }
    }
}