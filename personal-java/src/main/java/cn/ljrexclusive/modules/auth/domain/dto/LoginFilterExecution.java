package cn.ljrexclusive.modules.auth.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 过滤器执行记录
 */
@Data
@Builder
public class LoginFilterExecution {
    private String filterName;
    private LoginFilterResult result;
    private long duration;
    private long timestamp;
}