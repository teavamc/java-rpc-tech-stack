package com.teavamc.rpcserviceone.api;


import com.teavamc.rpcserviceone.api.model.UserModel;

/**
 *
 */
public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);
}
