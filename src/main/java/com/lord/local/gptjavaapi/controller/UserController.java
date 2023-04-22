package com.lord.local.gptjavaapi.controller;

import com.lord.local.gptjavaapi.dao.entity.User;
import com.lord.local.gptjavaapi.model.resultful.AuthResponse;
import com.lord.local.gptjavaapi.model.resultful.ChatBaseResponse;
import com.lord.local.gptjavaapi.service.JwtTokenProvider;
import com.lord.local.gptjavaapi.service.UserServer;
import com.lord.local.gptjavaapi.uitls.VliadatorUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServer _userServer;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @GetMapping("/list")
    public ChatBaseResponse<List<User>> getUsers() {
        List<User> users = _userServer.selectUsers();
        ChatBaseResponse<List<User>> remodel = ChatBaseResponse.successResponse(users);
        return remodel;
    }


    @ApiOperation(value = "创建用户", notes = "升级中")
    @PostMapping(value = "/create")
    public ChatBaseResponse<String> createUser(@RequestParam("account") String account, @RequestParam("password") String password) {
        return ChatBaseResponse.errorResponse("服务升级中", -1004);
//        if (!VliadatorUtil.validateAccount(account)) {
//            return ChatBaseResponse.errorResponse("用户名字母开头，允许5-16字节，允许字母数字下划线", -1001);
//        }
//        if (!VliadatorUtil.validatePassword(password)) {
//            return ChatBaseResponse.errorResponse("密码必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间", -1002);
//        }
//
//        Long uid = _userServer.createUser(account, password);
//        if (uid < 0L) {
//            return ChatBaseResponse.errorResponse("创建用户失败", -1003);
//        }
//        User authorization = _userServer.authorization(uid,0L);
//        String token = jwtTokenProvider.generateToken(authorization);
//        return ChatBaseResponse.successResponse(token);
    }

    @PostMapping("/getToken")
    public ChatBaseResponse<?> login(@RequestParam("account") String account, @RequestParam("password") String password) {
        if (!VliadatorUtil.validateAccount(account)) {
            return ChatBaseResponse.errorResponse("用户名字母开头，允许5-16字节，允许字母数字下划线", -1001);
        }
        if (!VliadatorUtil.validatePassword(password)) {
            return ChatBaseResponse.errorResponse("密码必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间", -1002);
        }
        User login = _userServer.login(account, password);
        if(login==null){
            return ChatBaseResponse.errorResponse("用户名或密码错误",-1005);
        }
        String token = jwtTokenProvider.generateToken(login);
        if(StringUtils.isEmpty(token)){
            return ChatBaseResponse.errorResponse("用户信息有更新请重新登录", -1010);
        }
        // Return response with custom authorization token
        ChatBaseResponse<AuthResponse> chatBaseResponse =ChatBaseResponse.successResponse(new AuthResponse("Bearer", token, JwtTokenProvider.EXPIRATION_TIME, "read write", account,login.getUid()));
        httpServletResponse.setHeader("Authorization", "Bearer " + token);
        return chatBaseResponse;
    }
}
