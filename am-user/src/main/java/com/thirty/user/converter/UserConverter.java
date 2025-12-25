package com.thirty.user.converter;

import com.thirty.user.model.dto.AddUserDTO;
import com.thirty.user.model.dto.ModifyUserDTO;
import com.thirty.user.model.dto.UpdateUserDTO;
import com.thirty.user.model.entity.Detail;
import com.thirty.user.model.entity.Role;
import com.thirty.user.model.entity.User;
import com.thirty.user.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Mapping(source = "detail.createTime", target = "createTime")
    @Mapping(source = "detail.updateTime", target = "updateTime")
    UserVO toUserVO(User user, Detail detail, List<Role> roles);

    /**
     * 将User列表、Detail列表和Role列表转换为UserVO列表
     */
    default List<UserVO> toUserVOS(List<User> users, List<Detail> details) {
        if (ObjectUtils.isEmpty(users) || ObjectUtils.isEmpty(details)) {
            return new ArrayList<>();
        }
        List<UserVO> userVOS = new ArrayList<>();
        users.forEach(user -> {
            Detail detail = details.stream()
                    .filter(d -> Objects.equals(d.getId(), user.getId()))
                    .findFirst()
                    .orElse(null);
            userVOS.add(toUserVO(user, detail, new ArrayList<>()));
        });
        return userVOS;
    }
}