package com.gxuwz.visitor.model.vo;

/**
 * 响应结果基类
 * @param <T>
 */
public class BaseResponse<T> {
    private String code;
    private String message;
    private T data;
}
