package cn.ljrexclusive.modules.auth.filter;

import cn.ljrexclusive.modules.auth.domain.dto.LoginFilterStatsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 过滤器监控服务
 */
@Service
@Slf4j
public class LoginFilterMonitorService {
    
    private final Map<String, LoginFilterStatsDTO> filterStats = new ConcurrentHashMap<>();
    
    /**
     * 记录过滤器执行
     */
    public void recordExecution(String filterName, boolean success, long duration) {
        LoginFilterStatsDTO stats = filterStats.computeIfAbsent(filterName,
                k -> new LoginFilterStatsDTO(filterName));
        
        stats.recordExecution(success, duration);
    }
    
    /**
     * 获取过滤器统计
     */
    public Map<String, LoginFilterStatsDTO> getFilterStatistics() {
        return new HashMap<>(filterStats);
    }
    
    /**
     * 重置统计
     */
    public void resetStatistics() {
        filterStats.clear();
    }

    
    private List<LoginFilterStatsDTO> findSlowFilters(List<LoginFilterStatsDTO> statsList) {
        return statsList.stream()
                .filter(stats -> stats.getAvgDuration() > 100) // 超过100ms的认为是慢过滤器
                .sorted(Comparator.comparingDouble(LoginFilterStatsDTO::getAvgDuration).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}