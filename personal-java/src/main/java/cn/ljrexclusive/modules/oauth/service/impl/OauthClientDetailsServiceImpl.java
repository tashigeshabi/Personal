package cn.ljrexclusive.modules.oauth.service.impl;

import cn.ljrexclusive.modules.oauth.entity.OauthClientDetails;
import cn.ljrexclusive.modules.oauth.mapper.OauthClientDetailsMapper;
import cn.ljrexclusive.modules.oauth.service.IOauthClientDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * OAuth2客户端表 服务实现类
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {

}
