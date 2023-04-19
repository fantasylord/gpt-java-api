package com.lord.local.gptjavaapi.controller;

import com.lord.local.gptjavaapi.dao.entity.User;
import com.lord.local.gptjavaapi.model.ChatBaseResponse;
import com.lord.local.gptjavaapi.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/user")
public class UserController {

    @Autowired
    private UserServer _userServer;

    @GetMapping("/list")
    public ChatBaseResponse<List<User>> getUsers() {
        List<User> users = _userServer.selectUsers();
        ChatBaseResponse<List<User>> remodel = new ChatBaseResponse();
        remodel.setData(users);
        return remodel;
    }
}
