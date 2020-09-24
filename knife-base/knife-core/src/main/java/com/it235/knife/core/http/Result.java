package com.it235.knife.core.http;

import com.it235.knife.core.exception.BaseException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;


/**
 * @Author Ron
 * @CreateTime 2020/9/22 14:45
 */
@ApiModel(description = "返回信息实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    @ApiModelProperty(value = "响应状态码[200-请求处理成功]",required = true)
    private int code;

    @ApiModelProperty(value = "提示消息",required = true)
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    @ApiModelProperty("扩展数据，特殊情况可使用")
    private Map<Object, Object> extra;

    /**
     * 判断当前对象是否为成功
     * @return true表示成功，false表示失败
     */
    public boolean isSuccess(){
        return this.code == ResultCode.OK.getCode();
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //成功区
    public static Result<String> ofSuccess() {
        return ofSuccess("ok");
    }

    public static Result<String> ofSuccess(String message) {
        return of(ResultCode.OK.getCode(), message, null);
    }

    public static <T> Result<T> ofSuccess(T data) {
        return ofStatus(ResultCode.OK, data);
    }

    //手动指定消息区
    public static <T> Result<T> ofMessage(String message, T data) {
        return of(ResultCode.OK.getCode(), message, data);
    }

    public static <T> Result<T> ofStatus(ResultCode status) {
        return ofStatus(status, (T) null);
    }

    public static <T> Result<T> ofStatus(ResultCode status, T data) {
        return of(status.getCode(), status.getMessage(), data);
    }

    public static <T> Result<T> of(Integer code, String message, T data) {
        return new Result(code, message, data);
    }

    //异常区
    public static <E extends BaseException, T> Result<T> ofException(E e) {
        return ofException(e, (T) null);
    }

    public static <E extends BaseException, T> Result<T> ofException(Integer errCode , String errMsg) {
        return of(errCode , errMsg , (T) null);
    }

    public static <E extends BaseException, T> Result<T> ofException(E t, T data) {
        return of(t.getCode(), t.getMessage(), data);
    }
}
