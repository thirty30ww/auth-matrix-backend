package com.thirty.system.controller;

import com.thirty.common.model.dto.ResultDTO;
import com.thirty.system.enums.model.SettingField;
import com.thirty.system.enums.result.SettingResultCode;
import com.thirty.system.model.dto.SettingDTO;
import com.thirty.system.model.vo.SettingVO;
import com.thirty.system.service.basic.SettingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    public ResultDTO<Object> getSetting(@RequestParam SettingField settingField) {
        return ResultDTO.of(SettingResultCode.GET_SETTING_SUCCESS, settingService.getSettingValue(settingField));
    }

    /**
     * 获取公共设置
     * @param settingField 设置字段
     * @return 设置值
     */
    @GetMapping("/public/get")
    public ResultDTO<Object> getPublicSetting(@RequestParam SettingField settingField) {
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

    /**
     * 修改所有设置
     * @param settingDTOS 设置DTO列表
     * @return 结果
     */
    @PostMapping("/modify")
    public ResultDTO<Void> modifySettings(@RequestBody List<SettingDTO> settingDTOS) {
        settingService.modifySettings(settingDTOS);
        return ResultDTO.of(SettingResultCode.MODIFY_SETTINGS_SUCCESS);
    }
}
