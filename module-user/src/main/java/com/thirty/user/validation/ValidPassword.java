package com.thirty.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

/**
 * 密码验证注解
 * 密码规则：
 * 1. 必须至少8位且包含数字和小写字母
 * 2. 必须包含大写字母或特殊字符(@或_)其中一种
 * 3. 只能包含数字、大小写字母、@、_
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern.List({
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z]).{8,}$",
        message = "新密码必须至少8位且包含数字和小写字母"
    ),
    @Pattern(
        regexp = "^(?=.*[A-Z@_]).+$",
        message = "新密码必须包含大写字母或特殊字符(@或_)其中一种"
    ),
    @Pattern(
        regexp = "^[a-zA-Z0-9@_]+$",
        message = "新密码只能包含数字、大小写字母、@、_"
    )
})
public @interface ValidPassword {
    String message() default "密码格式不正确";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}