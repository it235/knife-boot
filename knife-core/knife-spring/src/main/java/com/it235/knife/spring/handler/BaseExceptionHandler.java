package com.it235.knife.spring.handler;

import com.it235.knife.common.exception.BizException;
import com.it235.knife.common.exception.TokenException;
import com.it235.knife.common.http.Result;
import com.it235.knife.common.http.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;

/**
 * @description:
 * @author: jianjun.ren
 * @date: Created in 2020/9/25 13:37
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 参数校验异常
     * @param ex
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,ConstraintViolationException.class })
    public Result badRequestExceptionHandler(Exception ex, HttpServletRequest servletRequest,
                                             HttpServletResponse servletResponse) {
        log.error("请求[{}] [{}]参数校验不通过:{}", servletRequest.getMethod(), servletRequest.getRequestURI(), ex.getMessage());
        servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        StringBuilder errorMessage = new StringBuilder("");
        if (ex instanceof MethodArgumentNotValidException) {
            FieldError fieldError = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError();
            errorMessage.append(fieldError.getDefaultMessage());
        } else if (ex instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> violation : ((ConstraintViolationException) ex).getConstraintViolations()) {
                errorMessage.append(violation.getMessage());
            }
        } else {
            errorMessage.append(ex.getMessage());
        }
        return Result.ofException(HttpStatus.BAD_REQUEST.value(), errorMessage.toString());
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result accessExceptionHandler(Exception ex) {
        log.error("程序异常： because of {}", ex);
        return Result.ofException(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    /**
     * 无权限
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<?> handleException(TokenException ex) {
        log.error("程序异常 errorCode:{}, exception:{}", HttpStatus.UNAUTHORIZED.value(), ex);
        return Result.ofException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    /**
     * 文件或映射不存在
     * @param exception
     * @return
     */
    @ExceptionHandler({FileNotFoundException.class, NoHandlerFoundException.class})
    public Result<?> noFoundException(Exception exception) {
        log.error("程序异常 errorCode:{}, exception:{}", HttpStatus.NOT_FOUND.value(), exception);
        return Result.ofException(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    /**
     * 业务操作违规异常，由自定义的受检异常抛出
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BizException.class})
    public Result handleBizException(BizException ex) {
        log.warn("业务异常： because of {}", ex.getCode(), ex.getMessage());
        return Result.ofException(ex.getCode(), ex.getMessage());
    }

    /**
     * 未能被捕获的运行时异常
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class})
    public Result handleRuntimeException(RuntimeException ex) {
        log.error("【RuntimeException】:{}", ex.getMessage() , ex);
        return Result.ofException(ResultCode.INTERNAL_SERVER_ERROR.getCode(),ResultCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * 系统级别错误
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, Error.class, Throwable.class})
    public Result handleFatalErrorException(Throwable ex) {
        log.warn("fatal error ocurred: {}", ex.getMessage(), ex);
        String msg = "fatal error ocurred";
        if (ex != null) {
            msg += ":" + ex.getClass().getName() + ":" + ex.getMessage();
        }
        return Result.ofException(1 , msg);
    }
}
