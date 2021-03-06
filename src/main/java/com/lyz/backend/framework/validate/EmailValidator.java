package com.lyz.backend.framework.validate;


import com.lyz.backend.framework.util.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email,String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return ValidateUtils.isEmail(value);
    }

    @Override
    public void initialize(Email constraintAnnotation) {

    }

}
