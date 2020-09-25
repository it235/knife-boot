package com.it235.knife.core.exception;

import com.it235.knife.core.http.ResultCode;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/25 13:54
 */
public class BizException extends BaseException {
    public BizException(ResultCode resultCode) {
        super(resultCode);
    }

    public BizException(Integer code, String message) {
        super(code, message);
    }
}
