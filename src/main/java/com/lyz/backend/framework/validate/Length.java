package com.lyz.backend.framework.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证字符串长度
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
//指定验证器
@Constraint(validatedBy= LengthValidator.class)
@Documented
public @interface Length {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    //默认错误消息
    String message() default"{validate.length}";
    //分组
    Class<?>[] groups() default{};
    //负载
    Class<? extends Payload>[] payload() default{};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        org.hibernate.validator.constraints.Length[] value();
    }
}
