package com.it235.knife.core.http;

/**
 * @description: 定义返回结果枚举类型
 *
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 14:57
 */
public enum ResultCode implements IResultCode{
    OK(200, "操作成功"),
    PARAM_ERROR(400, "参数或参数类型错误"),
    UN_AUTHORIZED(401, "请求未授权"),
    NOT_FOUND(404, "404 没找到请求"),
    METHOD_NOT_ALLOW(405, "不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "不支持当前媒体类型"),
    REQUEST_REJECT(403, "请求被拒绝"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    SERVER_HYSTRIX(999, "服务器繁忙，请稍后重试"),
    FAILURE(1000, "业务异常");

    final int code;
    final String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}

interface IResultCode {
    String getMessage();
    int getCode();
}