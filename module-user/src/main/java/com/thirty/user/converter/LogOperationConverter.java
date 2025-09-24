package com.thirty.user.converter;

import com.thirty.user.model.entity.LogOperation;
import com.thirty.user.model.vo.LogOperationVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogOperationConverter {
    LogOperationConverter INSTANCE = Mappers.getMapper(LogOperationConverter.class);

    /**
     * 操作日志实体转换为操作日志VO
     * @param logOperation 操作日志实体
     * @return 操作日志VO
     */
    LogOperationVO toLogOperationVO(LogOperation logOperation);

    /**
     * 操作日志实体转换为操作日志VO
     * @param logOperation 操作日志实体
     * @param name 用户名称
     * @return 操作日志VO
     */
    LogOperationVO toLogOperationVO(LogOperation logOperation, String name);
}
