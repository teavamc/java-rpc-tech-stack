package com.teavamc.rpcapimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import com.teavamc.rpcapimanager.dao.dataobject.UserDO;
import com.teavamc.rpcapimanager.dao.mapper.UserMapper;
import com.teavamc.rpcapimanager.api.UserService;
import com.teavamc.rpcapimanager.api.model.UserModel;

/**
 *
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final BeanCopier copier = BeanCopier.create(UserModel.class, UserDO.class, false);

    public String getUserName(Long id) {
        UserDO userDO = userMapper.getById(id);
        return userDO != null ? userDO.getName() : null;
    }

    public UserModel addUser(UserModel user) {
        UserDO userDO = new UserDO();
        copier.copy(user, userDO, null);

        Long id = userMapper.insert(userDO);
        user.setId(id);
        return user;
    }
}
