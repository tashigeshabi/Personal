package cn.ljrexclusive.modules.auth.service;

import cn.ljrexclusive.modules.auth.config.LoginSecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录失败次数服务。
 *
 * <p>当前为内存实现，负责：</p>
 * <ul>
 *   <li>记录账号失败次数。</li>
 *   <li>达到阈值后进入临时锁定。</li>
 *   <li>登录成功后清空状态。</li>
 * </ul>
 *
 * <p>说明：此实现适合单实例或开发环境，多实例生产环境建议替换为 Redis/数据库持久化。</p>
 */
@Service
public class LoginAttemptService {

    /**
     * 锁定策略配置（最大失败次数、锁定时长）。
     */
    private final LoginSecurityProperties loginSecurityProperties;

    /**
     * 失败状态缓存，key 为归一化后的账号标识。
     */
    private final Map<String, AttemptState> attempts = new ConcurrentHashMap<>();

    public LoginAttemptService(LoginSecurityProperties loginSecurityProperties) {
        this.loginSecurityProperties = loginSecurityProperties;
    }

    /**
     * 判断账号是否处于锁定状态。
     *
     * <p>如果锁定已过期，会在这里自动清理状态，达到“自动解锁”的效果。</p>
     */
    public boolean isLocked(String account) {
        AttemptState state = attempts.get(accountKey(account));
        if (state == null) {
            return false;
        }
        if (state.lockUntilEpochSecond <= Instant.now().getEpochSecond()) {
            attempts.remove(accountKey(account));
            return false;
        }
        return true;
    }

    /**
     * 获取剩余锁定秒数，未锁定返回 0。
     */
    public long remainingLockSeconds(String account) {
        AttemptState state = attempts.get(accountKey(account));
        if (state == null) {
            return 0L;
        }
        long remain = state.lockUntilEpochSecond - Instant.now().getEpochSecond();
        return Math.max(remain, 0L);
    }

    /**
     * 登录成功后清理失败状态。
     */
    public void recordSuccess(String account) {
        attempts.remove(accountKey(account));
    }

    /**
     * 记录失败次数并根据阈值决定是否锁定。
     */
    public void recordFailure(String account) {
        String key = accountKey(account);
        AttemptState state = attempts.computeIfAbsent(key, k -> new AttemptState());
        state.failedCount++;
        if (state.failedCount >= loginSecurityProperties.getMaxFailedAttempts()) {
            state.lockUntilEpochSecond = Instant.now().getEpochSecond() + loginSecurityProperties.getLockDurationSeconds();
        }
    }

    /**
     * 账号归一化：去空格 + 小写，确保同一账号命中同一条计数记录。
     */
    private String accountKey(String account) {
        if (!StringUtils.hasText(account)) {
            return "_unknown_";
        }
        return account.trim().toLowerCase();
    }

    /**
     * 单账号失败状态。
     */
    private static class AttemptState {
        /**
         * 累计失败次数。
         */
        private int failedCount = 0;

        /**
         * 锁定截止时间（EpochSecond）。
         */
        private long lockUntilEpochSecond = 0;
    }
}
