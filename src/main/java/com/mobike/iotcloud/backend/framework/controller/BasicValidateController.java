package com.mobike.iotcloud.backend.framework.controller;

import com.mobike.iotcloud.backend.framework.exception.ValidateException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * 基础校验处理,所有的controller都需要继承该类，便于系统给出可读的错误提示信息
 */
@Slf4j
public class BasicValidateController{


    public void validate(BindingResult bindingResult){

        log.debug("validate error is running...");

        if (bindingResult.hasErrors()) {

            //获取一个校验错误
            ObjectError objectError = bindingResult.getAllErrors().get(0);

            if (objectError instanceof FieldError) {

                FieldError fieldError = (FieldError) objectError;
                throw new ValidateException(fieldError.getField()+":"+fieldError.getDefaultMessage());

            }else{

                throw new ValidateException(objectError.getObjectName()+":"+objectError.getDefaultMessage());
            }

        }

    }

    /**
     * 校验分页参数，不正确则
     * @param pageNo
     * @param pageSize
     */
    protected PagingParam validateAndSetPagingParam(Integer pageNo, Integer pageSize){

        return validateAndSetPagingParam(pageNo,pageSize,1,10);
    }


    protected PagingParam validateAndSetPagingParam(Integer pageNo, Integer pageSize, Integer defaultPageNo, Integer defaultPageSize){


        if (pageNo==null || pageNo.intValue()<1) {

            pageNo = defaultPageNo;
        }

        if (pageSize==null || pageSize.intValue()<0) {

            pageSize = defaultPageSize;
        }

        return new PagingParam(pageNo,pageSize);

    }

    @Data
    public static class PagingParam{

        private int pageNo;

        private int pageSize;

        public PagingParam(int pageNo, int pageSize){

            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }
    }
}
