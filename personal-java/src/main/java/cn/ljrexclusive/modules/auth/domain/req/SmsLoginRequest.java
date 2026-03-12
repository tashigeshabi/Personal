package cn.ljrexclusive.modules.auth.domain.req;

import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信验证码登录参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsLoginRequest extends LoginRequest {
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "验证码不能为空")
    @Size(min = 4, max = 6, message = "验证码长度4-6位")
    private String smsCode;
    
    @Override
    public void validate() {
        // TODO 可以添加手机号格式验证等
    }
}