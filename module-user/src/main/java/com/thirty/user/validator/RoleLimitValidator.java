package com.thirty.user.validator;

import com.thirty.system.api.SettingApi;
import com.thirty.user.annotation.ValidRoleLimit;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class RoleLimitValidator implements ConstraintValidator<ValidRoleLimit, List<Integer>> {
    @Resource
    private SettingApi settingApi;

    @Override
    public void initialize(ValidRoleLimit constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<Integer> roleIds, ConstraintValidatorContext context) {
        // 校验角色ID列表是否为空，空列表默认校验通过
        if (CollectionUtils.isEmpty(roleIds)) {
            return true;
        }

        // 获取用户角色数量限制
        Integer limit = settingApi.getUserRoleNumberLimit();
        // 校验用户角色数量是否超过限制
        boolean isValid = roleIds.size() <= limit;

        // 校验失败，添加自定义错误消息
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("用户角色数量不能超过%d个", limit))
                    .addConstraintViolation();
        }
        return isValid;
    }
}
