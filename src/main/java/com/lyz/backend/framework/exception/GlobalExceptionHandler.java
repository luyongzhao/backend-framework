package com.lyz.backend.framework.exception;

import com.alibaba.fastjson.JSON;
import com.lyz.backend.framework.controller.bean.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.NOT_EXTENDED;

/**
 * Created by wuwf on 17/3/31.
 * 全局异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 在controller里面内容执行之前，校验一些参数不匹配啊，Get post方法不对啊之类的
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        log.debug("internal exception is cathed...",ex);

        return new ResponseEntity<Object>(JSON.toJSONString(RestResponse.error("系统错误！")), NOT_EXTENDED);

    }


    /**
     * 校验异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = { ValidateException.class })
    @ResponseBody
    public RestResponse handleResourceNotFoundException(ValidateException e) {

        log.debug("validate error is catched...");

        return RestResponse.error(e.getMessage());
    }

    /**
     * 兜底异常处理
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestResponse jsonHandler(HttpServletRequest request, Exception e) throws Exception {

        log.debug("global exception is cathed...",e);

        return RestResponse.error("系统错误！");
    }
}