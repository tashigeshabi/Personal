package cn.ljrexclusive.modules.auth.service;


import cn.ljrexclusive.common.result.Result;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.res.UserLoginResponse;
import org.springframework.stereotype.Service;

/**
 * 登录接口
 */


public interface LoginService {

    /**
     * 账号密码登录
     * @param accountLoginRequest
     * @return
     */
    Result<UserLoginResponse> accountLogin(AccountLoginRequest accountLoginRequest);
}
