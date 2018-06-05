package com.mobike.iotcloud.backend.framework.validate;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LengthValidator implements ConstraintValidator<Length,String> {

    private int min;

    private int max;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        //最小值为0，则可以为空
        if (min <= 0) {

            return value == null||"".equals(value);

        }else{

            int length = value.length();

            return length>=min && length<=max;

        }



    }

    @Override
    public void initialize(Length constraintAnnotation) {

        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

}
