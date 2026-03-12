package cn.ljrexclusive.modules.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 过滤结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginFilterResult {
    
    /**
     * 是否通过
     */
    private boolean passed;
    
    /**
     * 错误码
     */
    private String errorCode;
    
    /**
     * 错误消息
     */
    private String errorMessage;
    
    /**
     * 额外数据
     */
    private Map<String, Object> extraData;
    
    /**
     * 成功结果
     */
    public static LoginFilterResult success() {
        return LoginFilterResult.builder()
                .passed(true)
                .build();
    }
    
    /**
     * 成功结果（带额外数据）
     */
    public static LoginFilterResult success(Map<String, Object> extraData) {
        return LoginFilterResult.builder()
                .passed(true)
                .extraData(extraData)
                .build();
    }
    
    /**
     * 失败结果
     */
    public static LoginFilterResult fail(String errorCode, String errorMessage) {
        return LoginFilterResult.builder()
                .passed(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}