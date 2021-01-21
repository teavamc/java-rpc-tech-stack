package com.teavamc.rpcserviceone.dao.mapper;

import com.teavamc.rpcserviceone.dao.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 */
@Mapper
public interface UserMapper {

    UserDO getByName(String name);

    UserDO getById(Long id);

    Long insert(UserDO userDO);
}
