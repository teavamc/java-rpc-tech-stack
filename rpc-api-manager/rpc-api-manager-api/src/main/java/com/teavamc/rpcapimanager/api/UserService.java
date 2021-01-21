package com.teavamc.rpcapimanager.api;


import com.teavamc.rpcapimanager.api.model.UserModel;

/**
 *
 */
public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);
}
