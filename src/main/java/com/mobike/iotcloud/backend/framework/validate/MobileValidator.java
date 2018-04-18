package com.mobike.iotcloud.backend.framework.validate;


import com.mobike.iotcloud.backend.framework.util.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile,String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return ValidateUtils.isMobile(value);
    }

    @Override
    public void initialize(Mobile constraintAnnotation) {

    }

}
