package com.lyz.backend.framework.validate;


import com.lyz.backend.framework.util.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone,String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return ValidateUtils.isPhone(value);
    }

    @Override
    public void initialize(Phone constraintAnnotation) {

    }

}
