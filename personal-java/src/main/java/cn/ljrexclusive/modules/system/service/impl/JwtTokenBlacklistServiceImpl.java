package cn.ljrexclusive.modules.system.service.impl;

import cn.ljrexclusive.modules.system.entity.JwtTokenBlacklist;
import cn.ljrexclusive.modules.system.mapper.JwtTokenBlacklistMapper;
import cn.ljrexclusive.modules.system.service.IJwtTokenBlacklistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * JWT令牌黑名单表 服务实现类
 * </p>
 *
 * @author Junery
 * @since 2026-01-16
 */
@Service
public class JwtTokenBlacklistServiceImpl extends ServiceImpl<JwtTokenBlacklistMapper, JwtTokenBlacklist> implements IJwtTokenBlacklistService {

}
