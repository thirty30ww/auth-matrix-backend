package com.thirty.user.annotation;

import com.thirty.user.validator.RoleLimitValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleLimitValidator.class)
public @interface ValidRoleLimit {
    String message() default "角色数量超过系统限制";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
