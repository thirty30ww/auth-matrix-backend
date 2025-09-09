package com.thirty.user.service.domain.view.builder;

import com.thirty.user.service.basic.ViewService;
import com.thirty.user.service.domain.view.ViewQueryDomain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewsBuilderFactory {
    @Resource
    private ViewService viewService;
    @Resource
    private ViewQueryDomain viewQueryDomain;

    public ViewsBuilder create() {
        return new ViewsBuilder(viewService, viewQueryDomain);
    }

    public ViewsBuilder create(List<Integer> currentAndChildRoleIds) {
        return create().forRoles(currentAndChildRoleIds);
    }

    public ViewsBuilder create(List<Integer> currentAndChildRoleIds, Integer targetRoleId) {
        return create(currentAndChildRoleIds).withTargetRole(targetRoleId);
    }
}
