package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.common.enums.ResultCode;
import cn.ljrexclusive.common.result.Result;
import cn.ljrexclusive.modules.auth.utils.JwtUtil;
import cn.ljrexclusive.modules.user.entity.SysUser;
import cn.ljrexclusive.modules.user.service.ISysUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";
    private static final String ACCESS_TOKEN_HEADER = "accessToken";
    private static final String BEARER_PREFIX = "Bearer ";

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 过滤请求：校验 access token 与 refresh token
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/login/")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources");
    }

    /**
     * 过滤请求：校验 access token 与 refresh token
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveAccessToken(request);
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.validateAccessToken(accessToken)) {
            if (applyAuthentication(request, accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            writeUnauthorized(request, response, "用户状态无效");
            return;
        }

        String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(refreshToken) && jwtUtil.validateRefreshToken(refreshToken)) {
            String refreshedAccessToken = refreshAccessToken(refreshToken);
            if (StringUtils.hasText(refreshedAccessToken) && applyAuthentication(request, refreshedAccessToken)) {
                response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + refreshedAccessToken);
                filterChain.doFilter(request, response);
                return;
            }
        }

        writeUnauthorized(request, response, "Token无效或已过期");
    }

    /**
     * 从请求头中解析 access token
     */
    private String resolveAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }
        return request.getHeader(ACCESS_TOKEN_HEADER);
    }

    /**
     * 应用认证：设置 SecurityContextHolder 中的 Authentication
     */
    private boolean applyAuthentication(HttpServletRequest request, String accessToken) {
        Long userId = jwtUtil.getUserIdFromAccessToken(accessToken);
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null || Boolean.FALSE.equals(sysUser.getStatus()) || Boolean.TRUE.equals(sysUser.getDeleted())) {
            return false;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                sysUser.getUsername(),
                null,
                Collections.emptyList()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        request.setAttribute("currentUserId", sysUser.getId());
        request.setAttribute("currentUsername", sysUser.getUsername());
        return true;
    }

    /**
     * 刷新 access token
     */
    private String refreshAccessToken(String refreshToken) {
        Long userId = jwtUtil.getUserIdFromRefreshToken(refreshToken);
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null || Boolean.FALSE.equals(sysUser.getStatus()) || Boolean.TRUE.equals(sysUser.getDeleted())) {
            return null;
        }
        String clientId = jwtUtil.getClientIdFromRefreshToken(refreshToken);
        String deviceId = jwtUtil.getDeviceIdFromRefreshToken(refreshToken);
        return jwtUtil.generateAccessToken(sysUser, clientId, deviceId);
    }

    /**
     * 刷新 access token
     */
    private void writeUnauthorized(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Result<Object> result = Result.failed(ResultCode.UNAUTHORIZED.getCode(), message);
        result.setPath(request.getRequestURI());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
