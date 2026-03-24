package cn.ljrexclusive.modules.auth.filter;


import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.enums.LoginFilterErrorCode;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import cn.ljrexclusive.modules.auth.service.LoginAttemptService;
import cn.ljrexclusive.modules.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 密码验证过滤器。
 *
 * <p>职责：</p>
 * <ul>
 *   <li>校验账号是否存在可用密码。</li>
 *   <li>校验账号是否被临时锁定。</li>
 *   <li>执行密码比对并更新失败计数状态。</li>
 * </ul>
 */
@Component
@Slf4j
public class LoginPasswordValidationFilter extends AbstractLoginFilter<LoginRequest> {

    /**
     * Spring Security 密码编码器，用于安全比对明文与密文。
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 登录失败次数服务，负责锁定策略与自动解锁。
     */
    private final LoginAttemptService loginAttemptService;

    public LoginPasswordValidationFilter(PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, LoginFilterMonitorService loginFilterMonitorService) {
        super(loginFilterMonitorService);
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public String getName() {
        return "loginPasswordValidationFilter";
    }
    
    @Override
    public int getOrder() {
        return 300;
    }
    
    @Override
    protected LoginFilterResult doFilterInternal(LoginFilterContext<LoginRequest> context) {
        LoginRequest request = context.getTarget();
        // 非账号密码登录，当前过滤器直接放行
        if (!(request instanceof AccountLoginRequest passwordRequest)) {
            return LoginFilterResult.success();
        }

        // 上游用户查询过滤器应先把用户放入上下文
        SysUser sysuser = context.getSysUser();
        if (sysuser == null) {
            return LoginFilterResult.fail(LoginFilterErrorCode.USER_NOT_FOUND.getCode(), LoginFilterErrorCode.USER_NOT_FOUND.getMessage());
        }
        // 防御式检查：避免空密码参与比对造成误判
        if (!StringUtils.hasText(sysuser.getPassword())) {
            return LoginFilterResult.fail(LoginFilterErrorCode.PASSWORD_NOT_SET.getCode(), LoginFilterErrorCode.PASSWORD_NOT_SET.getMessage());
        }

        try {
            // 命中锁定策略时直接拒绝登录
            if (loginAttemptService.isLocked(passwordRequest.getUsername())) {
                long remain = loginAttemptService.remainingLockSeconds(passwordRequest.getUsername());
                return LoginFilterResult.fail(LoginFilterErrorCode.ACCOUNT_LOCKED.getCode(), LoginFilterErrorCode.ACCOUNT_LOCKED.getMessage() + "（剩余" + remain + "秒）");
            }

            // 密码不匹配：记录失败次数并返回错误
            if (!passwordEncoder.matches(passwordRequest.getPassword(), sysuser.getPassword())) {
                loginAttemptService.recordFailure(passwordRequest.getUsername());
                return LoginFilterResult.fail(LoginFilterErrorCode.PASSWORD_NOT_CORRECT.getCode(), LoginFilterErrorCode.PASSWORD_NOT_CORRECT.getMessage());
            }

            // 密码匹配：清理失败状态，避免残留锁定
            loginAttemptService.recordSuccess(passwordRequest.getUsername());
            return LoginFilterResult.success();
        } catch (Exception e) {
            // 保底异常转码，避免技术细节暴露
            log.error("密码验证异常", e);
            return LoginFilterResult.fail(LoginFilterErrorCode.PASSWORD_VALIDATION_ERROR.getCode(), LoginFilterErrorCode.PASSWORD_VALIDATION_ERROR.getMessage());
        }
    }
}
