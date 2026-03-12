package cn.ljrexclusive.modules.user.service;

import cn.ljrexclusive.common.result.Result;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.auth.domain.res.UserLoginResponse;
import cn.ljrexclusive.modules.user.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    SysUser findByUsername(String username);   // 用户表


}
