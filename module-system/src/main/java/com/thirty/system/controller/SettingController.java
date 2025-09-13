package com.thirty.system.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.system.enums.model.SettingField;
import com.thirty.system.enums.result.SettingResultCode;
import com.thirty.system.model.vo.SettingVO;
import com.thirty.system.service.basic.SettingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setting")
public class SettingController {
    @Resource
    private SettingService settingService;

    /**
     * 获取用户设置
     * @param settingField 设置字段
     * @return 设置值
     */
    @GetMapping("/get")
    public ResultDTO<Object> getSetting(SettingField settingField) {
        return ResultDTO.of(SettingResultCode.GET_SETTING_SUCCESS, settingService.getSettingValue(settingField));
    }

    /**
     * 获取公共设置
     * @param settingField 设置字段
     * @return 设置值
     */
    @GetMapping("/public/get")
    public ResultDTO<Object> getPublicSetting(SettingField settingField) {
        return ResultDTO.of(SettingResultCode.GET_SETTING_SUCCESS, settingService.getPublicSettingValue(settingField));
    }

    /**
     * 获取所有设置值
     * @return 设置值
     */
    @GetMapping("/values")
    public ResultDTO<List<SettingVO>> getSettingVOS() {
        return ResultDTO.of(SettingResultCode.GET_SETTING_VALUES_SUCCESS, settingService.getSettingVOS());
    }
}
