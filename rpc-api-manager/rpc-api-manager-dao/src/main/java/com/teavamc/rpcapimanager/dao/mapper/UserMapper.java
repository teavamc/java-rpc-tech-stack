package com.teavamc.rpcapimanager.dao.mapper;

import com.teavamc.rpcapimanager.dao.dataobject.UserDO;
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
