package com.lyz.backend.framework.validate;

import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证是否为手机号
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
//指定验证器
@Constraint(validatedBy= MobileValidator.class)
@Documented
public @interface Mobile {

    //默认错误消息
    String message() default"{validate.mobile}";
    //分组
    Class<?>[] groups() default{};
    //负载
    Class<? extends Payload>[] payload() default{};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        Length[] value();
    }
}
