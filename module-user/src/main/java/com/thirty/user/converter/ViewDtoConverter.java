package com.thirty.user.converter;

import com.thirty.user.model.vo.ViewVO;
import com.thirty.user.model.entity.View;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * View实体与DTO之间的转换器
 */
@Mapper
public interface ViewDtoConverter {
    ViewDtoConverter INSTANCE = Mappers.getMapper(ViewDtoConverter.class);
    
    /**
     * 将View对象转换为ViewVO对象
     * @param view View对象
     * @return ViewVO对象
     */
    @Mapping(source = ".", target = "node")
    ViewVO toViewVO(View view);
    
    /**
     * 将View列表转换为ViewVO列表
     * @param views View列表
     * @return ViewVO列表
     */
    List<ViewVO> toViewVOS(List<View> views);
}