package cn.ljrexclusive.common.handler;

import cn.ljrexclusive.common.enums.ResultCode;
import cn.ljrexclusive.common.exception.BusinessException;
import cn.ljrexclusive.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

/**
 * 全局异常处理器（字符类型状态码）
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常，请求地址：{}，错误码：{}，异常信息：{}", 
                request.getRequestURI(), e.getCode(), e.getMessage());
        
        return Result.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    /**
     * 处理参数校验异常（@Validated注解）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("参数校验异常，请求地址：{}", request.getRequestURI());
        
        BindingResult bindingResult = e.getBindingResult();
        StringJoiner sj = new StringJoiner("; ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sj.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        
        return Result.builder()
                .code(ResultCode.PARAM_VALIDATE_FAILED.getCode())
                .message(sj.toString())
                .path(request.getRequestURI())
                .build();
    }

    /**
     * 处理参数绑定异常（表单提交）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("参数绑定异常，请求地址：{}", request.getRequestURI());
        
        BindingResult bindingResult = e.getBindingResult();
        StringJoiner sj = new StringJoiner("; ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sj.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }
        
        return Result.builder()
                .code(ResultCode.PARAM_VALIDATE_FAILED.getCode())
                .message(sj.toString())
                .path(request.getRequestURI())
                .build();
    }


    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常，请求地址：{}，异常信息：", request.getRequestURI(), e);
        
        return Result.builder()
                .code(ResultCode.INTERNAL_SERVER_ERROR.getCode())
                .message("系统繁忙，请稍后再试")
                .path(request.getRequestURI())
                .build();
    }
}