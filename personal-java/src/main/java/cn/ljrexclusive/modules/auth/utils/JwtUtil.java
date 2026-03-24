package cn.ljrexclusive.modules.auth.utils;

import cn.ljrexclusive.modules.auth.config.JwtConfig;
import cn.ljrexclusive.modules.auth.domain.req.AccountLoginRequest;
import cn.ljrexclusive.modules.user.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    /**
     * token 类型 claim 名称
     */
    private static final String TOKEN_TYPE_CLAIM = "type";
    /**
     * 访问令牌类型
     */
    private static final String TOKEN_TYPE_ACCESS = "access";
    /**
     * 刷新令牌类型
     */
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    @Resource
    private JwtConfig jwtConfig;

    /**
     * access token 签名密钥
     */
    private SecretKey accessSecretKey;
    /**
     * refresh token 签名密钥
     */
    private SecretKey refreshSecretKey;

    /**
     * 初始化签名密钥
     */
    @PostConstruct
    public void init() {
        accessSecretKey = Keys.hmacShaKeyFor(jwtConfig.getAccessKey().getBytes(StandardCharsets.UTF_8));
        refreshSecretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 access token
     */
    public String generateAccessToken(SysUser sysUser, AccountLoginRequest request) {
        return buildToken(sysUser, request.getClientId(), request.getDeviceId(), accessSecretKey, jwtConfig.getAccessTtl(), TOKEN_TYPE_ACCESS);
    }

    /**
     * 生成 refresh token
     */
    public String generateRefreshToken(SysUser sysUser, AccountLoginRequest request) {
        return buildToken(sysUser, request.getClientId(), request.getDeviceId(), refreshSecretKey, jwtConfig.getRefreshTtl(), TOKEN_TYPE_REFRESH);
    }

    /**
     * 生成 access token
     */
    public String generateAccessToken(SysUser sysUser, String clientId, String deviceId) {
        return buildToken(sysUser, clientId, deviceId, accessSecretKey, jwtConfig.getAccessTtl(), TOKEN_TYPE_ACCESS);
    }

    public String generateRefreshToken(SysUser sysUser, String clientId, String deviceId) {
        return buildToken(sysUser, clientId, deviceId, refreshSecretKey, jwtConfig.getRefreshTtl(), TOKEN_TYPE_REFRESH);
    }

    /**
     * 解析 access token
     */
    public Claims parseAccessToken(String token) {
        return parseToken(token, accessSecretKey);
    }

    /**
     * 解析 refresh token
     */
    public Claims parseRefreshToken(String token) {
        return parseToken(token, refreshSecretKey);
    }

    /**
     * 校验 access token（签名、类型、过期时间）
     */
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecretKey, TOKEN_TYPE_ACCESS);
    }

    /**
     * 校验 refresh token（签名、类型、过期时间）
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecretKey, TOKEN_TYPE_REFRESH);
    }

    /**
     * 从 access token 提取用户 ID
     */
    public Long getUserIdFromAccessToken(String token) {
        return Long.valueOf(parseAccessToken(token).getSubject());
    }

    /**
     * 从 access token 提取用户名
     */
    public String getUsernameFromAccessToken(String token) {
        return parseAccessToken(token).get("username", String.class);
    }

    public Long getUserIdFromRefreshToken(String token) {
        return Long.valueOf(parseRefreshToken(token).getSubject());
    }

    public String getClientIdFromRefreshToken(String token) {
        return parseRefreshToken(token).get("clientId", String.class);
    }

    public String getDeviceIdFromRefreshToken(String token) {
        return parseRefreshToken(token).get("deviceId", String.class);
    }

    /**
     * access token 过期秒数
     */
    public Long getAccessTtlSeconds() {
        return jwtConfig.getAccessTtl() / 1000;
    }

    /**
     * refresh token 过期秒数
     */
    public Long getRefreshTtlSeconds() {
        return jwtConfig.getRefreshTtl() / 1000;
    }

    /**
     * 构建 JWT：写入基础身份信息与 token 类型
     */
    private String buildToken(SysUser sysUser, String clientId, String deviceId, SecretKey secretKey, Long ttl, String tokenType) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ttl);
        return Jwts.builder()
                .subject(String.valueOf(sysUser.getId()))
                .claim("username", sysUser.getUsername())
                .claim("clientId", clientId)
                .claim("deviceId", deviceId)
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 按给定密钥解析 JWT payload
     */
    private Claims parseToken(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 通用校验：签名正确、类型匹配、未过期
     */
    private boolean validateToken(String token, SecretKey secretKey, String tokenType) {
        try {
            Claims claims = parseToken(token, secretKey);
            String claimType = claims.get(TOKEN_TYPE_CLAIM, String.class);
            Date expiration = claims.getExpiration();
            return tokenType.equals(claimType) && expiration != null && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}

