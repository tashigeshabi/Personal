package cn.ljrexclusive.modules.oauth.service.impl;

import cn.ljrexclusive.modules.oauth.entity.OauthRefreshToken;
import cn.ljrexclusive.modules.oauth.mapper.OauthRefreshTokenMapper;
import cn.ljrexclusive.modules.oauth.service.IOauthRefreshTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 刷新令牌表 服务实现类
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Service
public class OauthRefreshTokenServiceImpl extends ServiceImpl<OauthRefreshTokenMapper, OauthRefreshToken> implements IOauthRefreshTokenService {

}
