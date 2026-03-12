package cn.ljrexclusive.modules.auth.domain.res;


import cn.ljrexclusive.modules.user.domain.dto.UserInfoDto;
import lombok.Data;

@Data
public class UserLoginResponse {
    /**
     * 用户信息
     */
    private UserInfoDto userInfo;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 是否首次登录
     */
    private Boolean firstLogin = false;

    /**
     * 是否需要绑定手机/邮箱
     */
    private Boolean needBind = false;
}
