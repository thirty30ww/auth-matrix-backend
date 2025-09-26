package com.thirty.user.annotation;

import com.thirty.user.enums.model.LoginType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLog {
    LoginType type() default LoginType.LOGIN;
}
