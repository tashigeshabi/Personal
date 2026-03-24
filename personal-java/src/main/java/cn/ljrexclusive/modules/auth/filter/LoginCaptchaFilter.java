package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.common.exception.CaptchaException;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.enums.LoginFilterErrorCode;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 验证码过滤器
 */

@Component
@Slf4j
public class LoginCaptchaFilter extends AbstractLoginFilter<LoginRequest> {
    
    private final CaptchaService captchaService;

    public LoginCaptchaFilter(CaptchaService captchaService, LoginFilterMonitorService loginFilterMonitorService) {
        super(loginFilterMonitorService);
        this.captchaService = captchaService;
    }
    
    @Override
    public String getName() {
        return "loginCaptchaFilter";
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
    
    @Override
    protected LoginFilterResult doFilterInternal(LoginFilterContext<LoginRequest> context) {
        LoginRequest request = context.getTarget();

        if (!(request instanceof AccountLoginRequest passwordRequest)) {
            log.debug("跳过验证码验证，登录类型: {}", request.getLoginType());
            return LoginFilterResult.success();
        }

        if (StringUtils.hasText(passwordRequest.getCaptcha())) {
            if (!StringUtils.hasText(passwordRequest.getCaptchaKey())) {
                return LoginFilterResult.fail(LoginFilterErrorCode.CAPTCHA_KEY_MISSING.getCode(), LoginFilterErrorCode.CAPTCHA_KEY_MISSING.getMessage());
            }
            try {
                captchaService.verify(passwordRequest.getCaptchaKey(), passwordRequest.getCaptcha());
            } catch (CaptchaException e) {
                log.warn("验证码验证失败: {}", e.getMessage());
                return LoginFilterResult.fail(LoginFilterErrorCode.CAPTCHA_INVALID.getCode(), LoginFilterErrorCode.CAPTCHA_INVALID.getMessage());
            } catch (Exception e) {
                log.error("验证码验证异常", e);
                return LoginFilterResult.fail(LoginFilterErrorCode.CAPTCHA_SYSTEM_ERROR.getCode(), LoginFilterErrorCode.CAPTCHA_SYSTEM_ERROR.getMessage());
            }
            context.setAttribute("captchaVerified", true);
            return LoginFilterResult.success();
        }
        context.setAttribute("captchaVerified", false);
        return LoginFilterResult.success();
    }
}
