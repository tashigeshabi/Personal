package cn.ljrexclusive.common.result;

import cn.ljrexclusive.common.enums.ResultCode;
import cn.ljrexclusive.common.service.IResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果封装类（字符类型状态码）
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码（字符类型）
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 请求路径（可选）
     */
    private String path;

    /**
     * 跟踪ID（用于日志追踪）
     */
    private String traceId;

    /**
     * 请求ID（用于追踪单个请求）
     */
    private String requestId;

    public Result(IResult result) {
        this.code = result.getCode();
        this.message = result.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // ============== 成功响应 ==============
    
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    // ============== 失败响应 ==============
    
    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public static <T> Result<T> failed(IResult result) {
        return new Result<>(result);
    }

    public static <T> Result<T> failed(String code, String message) {
        return new Result<>(code, message);
    }

    public static <T> Result<T> failed(String code, String message, T data) {
        return new Result<>(code, message, data);
    }

    // ============== 便捷方法 ==============
    
    @JsonIgnore
    public boolean isSuccess() {
        return ResultCode.isSuccess(this.code);
    }

    @JsonIgnore
    public boolean isClientError() {
        return ResultCode.isClientError(this.code);
    }

    @JsonIgnore
    public boolean isServerError() {
        return ResultCode.isServerError(this.code);
    }

    public Result<T> path(String path) {
        this.path = path;
        return this;
    }

    public Result<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public Result<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    // ============== 常用业务方法 ==============
    
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.PARAM_VALIDATE_FAILED);
    }

    public static <T> Result<T> validateFailed(String message) {
        return failed(ResultCode.PARAM_VALIDATE_FAILED.getCode(), message);
    }

    public static <T> Result<T> unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }

    public static <T> Result<T> unauthorized(String message) {
        return failed(ResultCode.UNAUTHORIZED.getCode(), message);
    }

    public static <T> Result<T> forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }

    public static <T> Result<T> forbidden(String message) {
        return failed(ResultCode.FORBIDDEN.getCode(), message);
    }

    public static <T> Result<T> notFound() {
        return failed(ResultCode.NOT_FOUND);
    }

    public static <T> Result<T> notFound(String message) {
        return failed(ResultCode.NOT_FOUND.getCode(), message);
    }

    public static <T> Result<T> businessError(String message) {
        return failed(ResultCode.BUSINESS_ERROR.getCode(), message);
    }

    public static <T> Result<T> dataNotExist() {
        return failed(ResultCode.DATA_NOT_EXIST);
    }

    public static <T> Result<T> dataExisted() {
        return failed(ResultCode.DATA_EXISTED);
    }

    // ============== 构建器模式 ==============
    
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private String code;
        private String message;
        private T data;
        private String path;
        private String traceId;
        private String requestId;

        public Builder<T> code(String code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> path(String path) {
            this.path = path;
            return this;
        }

        public Builder<T> traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder<T> requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder<T> success() {
            this.code = ResultCode.SUCCESS.getCode();
            this.message = ResultCode.SUCCESS.getMessage();
            return this;
        }

        public Builder<T> success(String message) {
            this.code = ResultCode.SUCCESS.getCode();
            this.message = message;
            return this;
        }

        public Result<T> build() {
            Result<T> result = new Result<>(this.code, this.message, this.data);
            result.setPath(this.path);
            result.setTraceId(this.traceId);
            result.setRequestId(this.requestId);
            return result;
        }
    }
}