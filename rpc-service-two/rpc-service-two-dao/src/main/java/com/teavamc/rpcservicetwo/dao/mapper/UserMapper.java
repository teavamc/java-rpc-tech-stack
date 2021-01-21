package com.teavamc.rpcservicetwo.dao.mapper;

import com.teavamc.rpcservicetwo.dao.dataobject.UserDO;
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
