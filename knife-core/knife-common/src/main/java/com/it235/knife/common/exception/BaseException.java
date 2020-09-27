package com.it235.knife.common.exception;

import com.it235.knife.common.http.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 异常基础类
 *
 * @author: jianjun.ren
 * @date: Created in 2020/9/22 14:52
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private Integer code;
    private String message;

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
