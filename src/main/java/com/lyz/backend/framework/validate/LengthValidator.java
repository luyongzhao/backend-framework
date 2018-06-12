package com.lyz.backend.framework.validate;


import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LengthValidator implements ConstraintValidator<Length,String> {

    private int min;

    private int max;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {


        //最小值大于0，需要判断值不为空
        if (min > 0) {

            if  (StringUtils.isBlank(value)){

                return false;
            }

        }

        //非空时，需要判断长度是否超过最大值
        if (!StringUtils.isBlank(value)) {

            if (value.length() > max) {

                return false;
            }
        }else{

            if (max <= 0) {

                return false;
            }
        }


        return true;

    }

    @Override
    public void initialize(Length constraintAnnotation) {

        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

}
