package cn.ljrexclusive.modules.auth.service.impl;

import cn.ljrexclusive.common.exception.LoginFilterException;
import cn.ljrexclusive.common.exception.ResourceNotFoundException;
import cn.ljrexclusive.common.result.Result;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.res.UserLoginResponse;
import cn.ljrexclusive.modules.auth.filter.manager.FilterManager;
import cn.ljrexclusive.modules.auth.filter.service.FilterChain;
import cn.ljrexclusive.modules.auth.service.LoginService;
import cn.ljrexclusive.modules.auth.utils.JwtUtil;
import cn.ljrexclusive.modules.user.domain.dto.UserInfoDto;
import cn.ljrexclusive.modules.user.entity.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {


    @Resource
    private FilterManager filterManager;
    @Resource
    private JwtUtil jwtUtil;


    @Override
    public Result<UserLoginResponse> accountLogin(AccountLoginRequest accountLoginRequest) {

        long startTime = System.currentTimeMillis();
        accountLoginRequest.validate();


        // 1. 创建过滤上下文
        LoginFilterContext<AccountLoginRequest> context = LoginFilterContext.<AccountLoginRequest>builder()
                .target(accountLoginRequest)
                .loginRequest(accountLoginRequest)
                .attributes(new HashMap<>())
                .build();

        FilterChain<AccountLoginRequest> filterChain = filterManager.createLoginFilterChain(accountLoginRequest.getLoginType());

        // 3. 执行过滤链
        LoginFilterResult loginFilterResult = filterChain.doFilter(context);

        // 4. 检查过滤结果
        if (!loginFilterResult.isPassed()) {
            log.warn("登录过滤失败: {} - {}",
                    loginFilterResult.getErrorCode(), loginFilterResult.getErrorMessage());
            throw new LoginFilterException(loginFilterResult.getErrorCode(), loginFilterResult.getErrorMessage());
        }

        // 5. 获取用户信息
        SysUser sysUser = context.getSysUser();
        if (sysUser == null) {
            throw new ResourceNotFoundException("用户信息获取失败");
        }

        // 6. TODO生成Token
        String accessToken = jwtUtil.generateAccessToken(sysUser,accountLoginRequest);
        String refreshToken = jwtUtil.generateRefreshToken(sysUser,accountLoginRequest);

        // 7. TODO更新用户登录信息

        // 8. TODO构建结果(先简易测试)

        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setUserInfo(UserInfoDto.builder()
                .userId(sysUser.getId())
                .username(sysUser.getUsername())
                .nickname(sysUser.getNickname())
                .avatar(sysUser.getAvatar())
                .email(sysUser.getEmail())
                .phone(sysUser.getPhone())
                .build());
        userLoginResponse.setAccessToken(accessToken);
        userLoginResponse.setRefreshToken(refreshToken);
        userLoginResponse.setExpiresIn(7200L);

        log.info("账号登录成功, username={}, duration={}ms", sysUser.getUsername(), System.currentTimeMillis() - startTime);

        return Result.success(userLoginResponse);

    }
}
