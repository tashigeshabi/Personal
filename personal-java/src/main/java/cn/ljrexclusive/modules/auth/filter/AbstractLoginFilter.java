package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterContext;
import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterResult;
import cn.ljrexclusive.modules.auth.enums.LoginFilterErrorCode;
import cn.ljrexclusive.modules.auth.service.LoginFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录过滤器模板基类。
 *
 * <p>设计目的：</p>
 * <ul>
 *   <li>统一过滤器执行的外围流程，避免每个过滤器重复写样板代码。</li>
 *   <li>统一处理计时、异常兜底、执行历史记录和监控埋点。</li>
 *   <li>把业务判断下沉到 doFilterInternal，保证各过滤器关注点单一。</li>
 * </ul>
 */
@Slf4j
public abstract class AbstractLoginFilter<T> implements LoginFilter<T> {

    /**
     * 过滤器监控服务。
     *
     * <p>用于记录每个过滤器的成功/失败次数和耗时统计，
     * 便于后续做慢过滤器分析和稳定性治理。</p>
     */
    private final LoginFilterMonitorService loginFilterMonitorService;

    protected AbstractLoginFilter(LoginFilterMonitorService loginFilterMonitorService) {
        this.loginFilterMonitorService = loginFilterMonitorService;
    }

    /**
     * 模板方法：统一封装过滤器执行生命周期。
     *
     * <p>执行顺序：</p>
     * <ol>
     *   <li>记录开始时间。</li>
     *   <li>执行子类业务逻辑 doFilterInternal。</li>
     *   <li>兜底处理 null 返回和异常，统一转为 FILTER_EXCEPTION。</li>
     *   <li>记录执行历史到上下文。</li>
     *   <li>上报监控指标。</li>
     * </ol>
     */
    @Override
    public final LoginFilterResult filter(LoginFilterContext<T> context) {
        long start = System.currentTimeMillis();
        LoginFilterResult result;
        try {
            // 子类只负责业务判断与返回结果，不负责外围治理逻辑
            result = doFilterInternal(context);
            // 防御式编程：避免子类误返回 null 导致链路 NPE
            if (result == null) {
                result = LoginFilterResult.fail(LoginFilterErrorCode.FILTER_EXCEPTION.getCode(), LoginFilterErrorCode.FILTER_EXCEPTION.getMessage());
            }
        } catch (Exception e) {
            // 统一异常转码，避免技术异常泄漏到业务层
            log.error("过滤器 {} 执行异常", getName(), e);
            result = LoginFilterResult.fail(LoginFilterErrorCode.FILTER_EXCEPTION.getCode(), LoginFilterErrorCode.FILTER_EXCEPTION.getMessage());
        }
        long duration = System.currentTimeMillis() - start;
        // 执行轨迹写入上下文，便于登录诊断和链路追踪
        context.recordExecution(this, result, duration);
        if (loginFilterMonitorService != null) {
            // 上报监控：名称、成功状态、耗时
            loginFilterMonitorService.recordExecution(getName(), result.isPassed(), duration);
        }
        return result;
    }

    /**
     * 子类实现点：仅实现当前过滤器的业务规则。
     */
    protected abstract LoginFilterResult doFilterInternal(LoginFilterContext<T> context);
}
