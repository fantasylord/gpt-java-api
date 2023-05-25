package com.lord.local.gptjavaapi.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lord.local.gptjavaapi.constant.BaseConstant;
import com.lord.local.gptjavaapi.dao.UserDao;
import com.lord.local.gptjavaapi.dao.entity.User;
import com.lord.local.gptjavaapi.dao.entity.UserExample;
import com.lord.local.gptjavaapi.uitls.ScryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServer {


    @Autowired
    private MyRedisService _myRedisService;

    @Autowired
    private UserDao _userDao;

    private String user_cache_format="user:uid_%d";
    private Long EXPIRATION_MILLISECONDS_USER=6*10*1000L;
    public  User selectUserCache(Long uid){
        if(uid<=0){
            return null;
        }
        String userJson= _myRedisService.readValueForStr(String.format(user_cache_format,uid));
        User user = JSONObject.parseObject(userJson, User.class);
        if(user==null){
            User userDto = selectUser(uid);
            if(userDto!=null){
                boolean result = _myRedisService.putString(String.format(user_cache_format,uid), JSONObject.toJSONString(userDto), EXPIRATION_MILLISECONDS_USER);
                if(!result){
                    log.error("set user {} on cache error ",uid);
                }
            }
            return userDto;
        }
        return user;
    }
    private   User selectUser(Long uid){
        User selectModel = new User();
        List<User> users = null;
        try {
            UserExample example = new UserExample();
            example.setLimit(1);
            example.setOrderByClause("create_time desc");
            UserExample.Criteria or = example.or();
            or.andUidEqualTo(uid);
            or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);
            users = _userDao.selectByExample(example);
            return  users.stream().findFirst().orElse(null);
        } catch (Exception e) {
            log.error("error,msg:{}", e.getMessage(), e);
        }
        return null;
    }
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


    public Long createUser(String account, String password) {
        User user = new User();
        user.setAccount(account);
        try {
            String ps = ScryptUtil.hashPassword(password);
            user.setPasswordSecret(ps);
            user.setAccount(account);
            _userDao.insertSelective(user);
        } catch (Exception e) {
            log.error("password is not right,:{}", password);
        }
        return user.getUid() == null ? 0 : user.getUid();
    }
    public User authorization(String userAccount,Long version) {
        UserExample example = new UserExample();
        example.setLimit(1);
        example.setOrderByClause("create_time desc");
        UserExample.Criteria or = example.or();
        or.andAccountEqualTo(userAccount);
        or.andVersionEqualTo(version);
        or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);

        List<User> users = _userDao.selectByExample(example);
        User user = users.stream().findFirst().orElse(null);
        return user;
    }
    public User authorization(Long uid,Long version) {
        UserExample example = new UserExample();
        example.setLimit(1);
        example.setOrderByClause("create_time desc");
        UserExample.Criteria or = example.or();
        or.andUidEqualTo(uid);
        or.andVersionEqualTo(version);
        or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);

        List<User> users = _userDao.selectByExample(example);
        User user = users.stream().findFirst().orElse(null);
        return user;
    }
    public User login(String account, String password) {
        UserExample example = new UserExample();
        example.setLimit(1);
        example.setOrderByClause("create_time desc");
        UserExample.Criteria or = example.or();
        or.andAccountEqualTo(account);
        or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);

        List<User> users = _userDao.selectByExample(example);

        User user = users.stream().findFirst().orElse(null);
        if (user == null) {
            return null;
        }
        try {
            ScryptUtil.verifyPassword(password, user.getPasswordSecret());
        } catch (Exception e) {
            log.error("password error,msg:{}", e.getMessage(), e);
            return null;
        }
        user.setPasswordSecret(null);
        user.setSecretType(null);
        return user;
    }

//    public UserDetails certification(String userAccount, String password) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        SimpleGrantedAuthority user = new SimpleGrantedAuthority("USER");
//        authorities.add(user);
//        bCryptPasswordEncoder.encode(password);
//        return new org.springframework.security.core.userdetails.User(userAccount, password, authorities);
//    }

}
