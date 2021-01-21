package com.teavamc.rpcconsole.api;


import com.teavamc.rpcconsole.api.model.UserModel;

/**
 *
 */
public interface UserService {

    String getUserName(Long id);

    UserModel addUser(UserModel user);
}
