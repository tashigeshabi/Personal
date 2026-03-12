package cn.ljrexclusive.modules.auth.domain.req;

import cn.ljrexclusive.modules.auth.domain.req.basic.LoginRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信登录参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WechatLoginRequest extends LoginRequest {
    
    @NotBlank(message = "微信code不能为空")
    private String code;
    
    private String state;
    
    @Override
    public void validate() {
        // TODO 微信登录参数验证
    }
}