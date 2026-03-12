package cn.ljrexclusive.modules.auth.domain.req;

import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 密码登录参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountLoginRequest extends LoginRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20个字符")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20个字符")
    private String password;

    
    /**
     * 验证码
     */
    private String captcha;
    
    /**
     * 验证码标识
     */
    private String captchaKey;
    
    @Override
    public void validate() {
        // TODO 这里可以添加密码强度验证等
        if (password.length() < 6) {
            throw new IllegalArgumentException("密码强度不足");
        }
    }
}