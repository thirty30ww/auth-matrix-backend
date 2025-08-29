package com.thirty.user.converter;

import com.thirty.user.model.dto.AddUserDTO;
import com.thirty.user.model.dto.ModifyUserDTO;
import com.thirty.user.model.dto.UpdateUserDTO;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.vo.UserVO;
import com.thirty.user.model.entity.Detail;
import com.thirty.user.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * DTO与实体类之间的转换器
 */
@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * 将AddUserDTO对象转换为Detail对象
     */
    Detail addUserDTOToDetail(AddUserDTO request);

    /**
     * 将ModifyUserDTO对象转换为Detail对象
     */
    Detail modifyUserDTOToDetail(ModifyUserDTO request);

    /**
     * 将UpdateUserDTO对象转换为Detail对象
     */
    Detail updateUserDTOToDetail(UpdateUserDTO request);
    
    /**
     * 将User和Detail对象转换为UserVO对象
     */
    @Mapping(source = "user.id", target = "id")
    UserVO toUserResponse(User user, Detail detail, List<Role> roles);
}