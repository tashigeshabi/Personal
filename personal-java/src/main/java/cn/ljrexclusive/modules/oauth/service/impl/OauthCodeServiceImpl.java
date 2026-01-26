package cn.ljrexclusive.modules.oauth.service.impl;

import cn.ljrexclusive.modules.oauth.entity.OauthCode;
import cn.ljrexclusive.modules.oauth.mapper.OauthCodeMapper;
import cn.ljrexclusive.modules.oauth.service.IOauthCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * OAuth2授权码表 服务实现类
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Service
public class OauthCodeServiceImpl extends ServiceImpl<OauthCodeMapper, OauthCode> implements IOauthCodeService {

}
