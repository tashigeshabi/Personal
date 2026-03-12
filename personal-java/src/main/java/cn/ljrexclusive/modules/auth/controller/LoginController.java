package cn.ljrexclusive.modules.auth.controller;


import cn.ljrexclusive.common.result.Result;
import cn.ljrexclusive.modules.auth.config.GrantorConfig;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.res.UserLoginResponse;
import cn.ljrexclusive.modules.auth.enums.LoginType;
import cn.ljrexclusive.modules.auth.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户登录相关
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private GrantorConfig grantorConfig;
    @Resource
    private LoginService loginService;

    @GetMapping("test")
    public Result<Map<String,String>> login() {
        return Result.success("测试成功",grantorConfig.getTypes());
    }


    @PostMapping("/loginByAccount")
    public Result<UserLoginResponse> loginByAccount(@Valid @RequestBody AccountLoginRequest accountLoginRequest,
                                                    HttpServletRequest httpServletRequest) {
        accountLoginRequest.setLoginType(LoginType.PASSWORD.getType());
        if (!StringUtils.hasText(accountLoginRequest.getIp())) {
            accountLoginRequest.setIp(httpServletRequest.getRemoteAddr());
        }
        if (!StringUtils.hasText(accountLoginRequest.getUserAgent())) {
            accountLoginRequest.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        }
        if (!StringUtils.hasText(accountLoginRequest.getDeviceId())) {
            accountLoginRequest.setDeviceId(httpServletRequest.getHeader("Device-Id"));
        }
        return loginService.accountLogin(accountLoginRequest);
    }
}
