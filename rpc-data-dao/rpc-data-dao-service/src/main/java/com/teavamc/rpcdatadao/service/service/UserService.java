package com.teavamc.rpcdatadao.service.service;


import com.teavamc.rpcdatadao.api.model.UserModel;

/**
 *
 */
public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);
}
