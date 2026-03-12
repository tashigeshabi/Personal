package cn.ljrexclusive.common.utils;


import cn.ljrexclusive.common.exception.BusinessException;
import cn.ljrexclusive.common.service.IResult;

/**
 * 响应工具类（字符类型状态码）
 */
public class ResponseUtils {

    /**
     * 断言为真，否则抛出业务异常
     */
    public static void isTrue(boolean expression, IResult result) {
        if (!expression) {
            throw new BusinessException(result);
        }
    }

    /**
     * 断言为真，否则抛出业务异常
     */
    public static void isTrue(boolean expression, String code, String message) {
        if (!expression) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * 断言为真，否则抛出业务异常
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    /**
     * 断言非空，否则抛出业务异常
     */
    public static <T> T notNull(T object, IResult result) {
        if (object == null) {
            throw new BusinessException(result);
        }
        return object;
    }

    /**
     * 断言非空，否则抛出业务异常
     */
    public static <T> T notNull(T object, String code, String message) {
        if (object == null) {
            throw new BusinessException(code, message);
        }
        return object;
    }

    /**
     * 断言非空，否则抛出业务异常
     */
    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
        return object;
    }

    /**
     * 断言表达式为假，否则抛出业务异常
     */
    public static void isFalse(boolean expression, IResult result) {
        if (expression) {
            throw new BusinessException(result);
        }
    }

    /**
     * 断言字符串不为空，否则抛出业务异常
     */
    public static String notEmpty(String str, IResult result) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(result);
        }
        return str;
    }

    /**
     * 断言字符串不为空，否则抛出业务异常
     */
    public static String notEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(message);
        }
        return str;
    }
}