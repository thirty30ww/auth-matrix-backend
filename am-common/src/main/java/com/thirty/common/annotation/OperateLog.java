package com.thirty.common.annotation;

import com.thirty.common.enums.model.OperationType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
    String description() default "";
    OperationType type() default OperationType.SELECT;
}