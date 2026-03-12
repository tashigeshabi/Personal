package cn.ljrexclusive.common.service;


import java.io.Serializable;

/**
 * 返回结果基础接口
 */
public interface IResult extends Serializable {

    /**
     *  状态码
     */
    String getCode();

    /**
     * 获取信息
     */
    String getMessage();
}
