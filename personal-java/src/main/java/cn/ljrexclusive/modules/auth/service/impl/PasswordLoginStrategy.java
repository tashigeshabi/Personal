//package cn.ljrexclusive.modules.auth.service.impl;
//
//import cn.ljrexclusive.modules.auth.domain.req.PasswordLoginRequest;
//import cn.ljrexclusive.modules.auth.service.CaptchaService;
//import cn.ljrexclusive.modules.auth.service.LoginStrategyService;
//import cn.ljrexclusive.modules.user.service.ISysUserService;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
///**
// * 密码登录策略
// */
//@Component
//public class PasswordLoginStrategy implements LoginStrategyService<PasswordLoginRequest> {
//
//    @Resource
//    private ISysUserService iSysUserService;
//
//    @Resource
//    private PasswordEncoder passwordEncoder;
//
//    @Resource
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Resource
//    private CaptchaService captchaService;
//
//
//    @Override
//    public LoginResult login(PasswordLoginRequest request) {
//        // 1. 验证验证码（如果有）
//        if (StringUtils.hasText(request.getCaptcha())) {
//            captchaService.verify(request.getCaptchaKey(), request.getCaptcha());
//        }
//
//        // 2. 查找用户
//        sysUser user = userService.findByUsername(request.getUsername());
//        if (user == null) {
//            throw new AuthenticationException("用户不存在");
//        }
//
//        // 3. 验证密码
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new AuthenticationException("密码错误");
//        }
//
//        // 4. 验证账户状态
//        if (!user.getEnabled()) {
//            throw new AuthenticationException("账户已被禁用");
//        }
//
//        // 5. 生成令牌
//        String accessToken = jwtTokenUtil.generateToken(user);
//        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
//
//        // 6. 构建用户信息
//        UserInfo userInfo = UserInfo.builder()
//                .userId(user.getId())
//                .username(user.getUsername())
//                .nickname(user.getNickname())
//                .avatar(user.getAvatar())
//                .email(user.getEmail())
//                .phone(user.getPhone())
//                .roles(userService.getUserRoles(user.getId()))
//                .build();
//
//        return LoginResult.builder()
//                .userInfo(userInfo)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .expiresIn(jwtTokenUtil.getExpiration())
//                .build();
//    }
//
//    @Override
//    public void preLogin(PasswordLoginRequest request) {
//        // 增强的预处理：检查登录失败次数等
//        String username = request.getUsername();
//        int failCount = userService.getLoginFailCount(username);
//        if (failCount >= 5) {
//            throw new AuthenticationException("登录失败次数过多，请15分钟后再试");
//        }
//        super.preLogin(request);
//    }
//}