package com.teavamc.rpcgateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.teavamc.rpcgateway.api.UserService;
import com.teavamc.rpcgateway.api.model.UserModel;

/**
 *
 */
@Component
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/username")
    public String getUserName(@RequestParam("id") Long id) {
        return userService.getUserName(id);
    }

    @RequestMapping("/add")
    @ResponseBody
    public UserModel addUser(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        UserModel user = new UserModel();
        user.setName(name);
        user.setAge(age);
        return userService.addUser(user);
    }
}
