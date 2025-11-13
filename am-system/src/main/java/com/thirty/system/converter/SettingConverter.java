package com.thirty.system.converter;

import com.thirty.system.model.entity.Setting;
import com.thirty.system.model.vo.SettingVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SettingConverter {
    SettingConverter INSTANCE = Mappers.getMapper(SettingConverter.class);

    /**
     * 将设置实体转换为设置VO
     * @param setting 设置实体
     * @return 设置VO
     */
    SettingVO toSettingVO(Setting setting);

    /**
     * 将设置实体列表转换为设置VO列表
     * @param settings 设置实体列表

    @Override
    public List<SettingVO> toSettingVOS(List<Setting> settings) {
        if ( settings == null ) {
            return null;
        }

        List<SettingVO> list = new java.util.ArrayList<SettingVO>( settings.size() );
        for ( Setting setting : settings ) {
            list.add( toSettingVO( setting ) );
        }

        return list;
    }
     * @return 设置VO列表
     */
    List<SettingVO> toSettingVOS(List<Setting> settings);
}
