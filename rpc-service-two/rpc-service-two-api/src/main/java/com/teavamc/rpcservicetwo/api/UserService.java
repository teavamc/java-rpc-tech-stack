package com.teavamc.rpcservicetwo.api;


import com.teavamc.rpcservicetwo.api.model.UserModel;

/**
 *
 */
public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);
}
