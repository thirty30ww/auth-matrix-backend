package com.thirty.user.converter;

import com.thirty.user.model.dto.ViewDTO;
import com.thirty.user.model.entity.View;
import com.thirty.user.model.vo.ViewVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * View实体与DTO之间的转换器
 */
@Mapper
public interface ViewConverter {
    ViewConverter INSTANCE = Mappers.getMapper(ViewConverter.class);
    
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

    /**
     * 将View列表转换为ViewVO Map，key为视图ID，value为ViewVO
     * @param views View列表
     * @return ViewVO Map
     */
    default Map<Integer, ViewVO> toViewVOMap(List<View> views) {
        return views.stream().collect(Collectors.toMap(View::getId, this::toViewVO));
    }

    /**
     * 将ViewDTO对象转换为View对象
     * @param viewDTO ViewDTO对象
     * @return View对象
     */
    View toView(ViewDTO viewDTO);
}