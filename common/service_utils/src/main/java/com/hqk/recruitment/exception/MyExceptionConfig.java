package com.hqk.recruitment.exception;


import com.hqk.recruitment.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;
import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class MyExceptionConfig {

    /**
     * 封装返回信息
     * 不建议直接对 Exception 进行处理，最好是根据各类异常作分别处理
     * 可以通过查看springboot开源框架中 ResponseEntityExceptionHandler类的方法 handleException
     * 的源代码来分别处理
     *
     * @param ex
     * @return
     */
    private R getExceptionMessage(Exception ex) {
        log.error(ex.getMessage());
        if (ex instanceof NullPointerException) {
            return R.error().message("系统错误：空指针异常");
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.METHOD_NOT_ALLOWED.value());
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.NOT_ACCEPTABLE.value());
        } else if (ex instanceof MissingPathVariableException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (ex instanceof MissingServletRequestParameterException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof ServletRequestBindingException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof ConversionNotSupportedException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (ex instanceof TypeMismatchException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof HttpMessageNotReadableException) {
            return R.error().message("参数异常").code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof HttpMessageNotWritableException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (ex instanceof MethodArgumentNotValidException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof MissingServletRequestPartException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof BindException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.BAD_REQUEST.value());
        } else if (ex instanceof NoHandlerFoundException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.NOT_FOUND.value());
        } else if (ex instanceof AsyncRequestTimeoutException) {
            return R.error().message(ex.getMessage()).code(HttpStatus.SERVICE_UNAVAILABLE.value());
        }
        return R.error().message("未知错误，请联系管理员").code(500);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R parameterExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            FieldError fieldError = (FieldError) errors.get(0);
            return R.error().message(fieldError.getDefaultMessage());
        } else {
            return R.error().message("参数绑定未知错误");
        }
    }

    @ExceptionHandler(value =MyCustomException.class)
    public R myCustomException(MyCustomException e){
        log.error(e.getMsg());
        return R.error().message(e.getMsg()).code(e.getCode());
    }

    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        return R.error().message(e.getMessage());
    }
}