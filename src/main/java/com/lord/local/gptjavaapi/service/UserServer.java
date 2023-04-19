package com.lord.local.gptjavaapi.service;

import com.lord.local.gptjavaapi.dao.UserDao;
import com.lord.local.gptjavaapi.dao.entity.User;
import com.lord.local.gptjavaapi.dao.entity.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServer {


    @Autowired
    private UserDao _userDao;



    public List<User> selectUsers() {
        User selectModel = new User();
        List<User> users = null;
        try {
            UserExample example = new UserExample();
            users = _userDao.selectByExample(example);
        } catch (Exception e) {
            log.error("error,msg:{}", e.getMessage(), e);
        }
        return users;
    }

}
