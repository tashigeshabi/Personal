package cn.ljrexclusive.modules.oauth.service.impl;

import cn.ljrexclusive.modules.oauth.entity.OauthAccessToken;
import cn.ljrexclusive.modules.oauth.mapper.OauthAccessTokenMapper;
import cn.ljrexclusive.modules.oauth.service.IOauthAccessTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 访问令牌表 服务实现类
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Service
public class OauthAccessTokenServiceImpl extends ServiceImpl<OauthAccessTokenMapper, OauthAccessToken> implements IOauthAccessTokenService {

}
