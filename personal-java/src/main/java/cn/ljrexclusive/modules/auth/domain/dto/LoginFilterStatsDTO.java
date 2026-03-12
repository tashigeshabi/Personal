package cn.ljrexclusive.modules.auth.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 过滤器统计
 */
@Data
public class LoginFilterStatsDTO {
    private String filterName;
    private long executionCount;
    private long successCount;
    private long failureCount;
    private long totalDuration;
    private double avgDuration;
    private double successRate;
    private LocalDateTime lastExecution;
    
    public LoginFilterStatsDTO(String filterName) {
        this.filterName = filterName;
    }
    
    public synchronized void recordExecution(boolean success, long duration) {
        executionCount++;
        if (success) {
            successCount++;
        } else {
            failureCount++;
        }
        totalDuration += duration;
        avgDuration = (double) totalDuration / executionCount;
        successRate = (double) successCount / executionCount * 100;
        lastExecution = LocalDateTime.now();
    }
}