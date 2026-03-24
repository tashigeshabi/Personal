package cn.ljrexclusive.modules.auth;

import cn.ljrexclusive.common.exception.CaptchaException;
import cn.ljrexclusive.modules.auth.service.CaptchaService;
import cn.ljrexclusive.modules.user.entity.SysUser;
import cn.ljrexclusive.modules.user.service.ISysUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 登录主链路集成测试。
 *
 * <p>测试范围：控制器 -> 登录服务 -> 过滤器链 -> 统一返回。</p>
 * <p>为了聚焦认证逻辑，测试中排除了数据库自动配置，并通过 MockBean 模拟外部依赖。</p>
 */
@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration,com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration"
})
@AutoConfigureMockMvc
class LoginFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ISysUserService sysUserService;
    @MockBean
    private CaptchaService captchaService;

    @Test
    void shouldLoginSuccess() throws Exception {
        // 准备一个正常账号，覆盖“成功登录并返回双 token”场景
        SysUser user = buildUser("ok_user", "123456", true, false);
        when(sysUserService.findByUsername(eq("ok_user"))).thenReturn(user);

        mockMvc.perform(post("/login/loginByAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("ok_user", "123456", null, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    @Test
    void shouldFailWhenUserNotFound() throws Exception {
        // 用户查询返回空，验证 USER_NOT_FOUND 分支
        when(sysUserService.findByUsername(eq("no_user"))).thenReturn(null);

        mockMvc.perform(post("/login/loginByAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("no_user", "123456", null, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("F2001"))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    @Test
    void shouldFailWhenPasswordIncorrect() throws Exception {
        // 用户存在但密码错误，验证密码校验失败分支
        SysUser user = buildUser("wrong_pwd_user", "654321", true, false);
        when(sysUserService.findByUsername(eq("wrong_pwd_user"))).thenReturn(user);

        mockMvc.perform(post("/login/loginByAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("wrong_pwd_user", "123456", null, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("F3002"))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }

    @Test
    void shouldFailWhenAccountDisabled() throws Exception {
        // 用户被禁用，验证账号状态过滤器分支
        SysUser user = buildUser("disabled_user", "123456", false, false);
        when(sysUserService.findByUsername(eq("disabled_user"))).thenReturn(user);

        mockMvc.perform(post("/login/loginByAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("disabled_user", "123456", null, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("F4001"))
                .andExpect(jsonPath("$.message").value("账号已禁用"));
    }

    @Test
    void shouldFailWhenCaptchaInvalid() throws Exception {
        // 注入验证码异常，验证验证码失败分支
        doThrow(new CaptchaException("invalid")).when(captchaService).verify(anyString(), anyString());

        mockMvc.perform(post("/login/loginByAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body("captcha_user", "123456", "abc", "k1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("F1002"))
                .andExpect(jsonPath("$.message").value("验证码错误"));
    }

    /**
     * 构造测试用户实体。
     */
    private SysUser buildUser(String username, String rawPassword, Boolean status, Boolean deleted) {
        SysUser sysUser = new SysUser();
        sysUser.setId(1L);
        sysUser.setUsername(username);
        sysUser.setPassword(passwordEncoder.encode(rawPassword));
        sysUser.setStatus(status);
        sysUser.setDeleted(deleted);
        return sysUser;
    }

    /**
     * 构造登录请求 JSON。
     */
    private String body(String username, String password, String captcha, String captchaKey) throws Exception {
        LoginBody loginBody = new LoginBody();
        loginBody.username = username;
        loginBody.password = password;
        loginBody.captcha = captcha;
        loginBody.captchaKey = captchaKey;
        return objectMapper.writeValueAsString(loginBody);
    }

    /**
     * 用于序列化请求体的轻量 DTO。
     */
    private static class LoginBody {
        public String username;
        public String password;
        public String captcha;
        public String captchaKey;
    }
}
