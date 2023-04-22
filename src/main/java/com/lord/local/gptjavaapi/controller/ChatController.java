package com.lord.local.gptjavaapi.controller;


import com.lord.local.gptjavaapi.dao.entity.ChatContent;
import com.lord.local.gptjavaapi.dao.entity.User;
import com.lord.local.gptjavaapi.model.resultful.*;
import com.lord.local.gptjavaapi.remoter.client.OpenAIUserClient;
import com.lord.local.gptjavaapi.remoter.client.OpenChatClient;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelDataResponse;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelResponse;
import com.lord.local.gptjavaapi.service.ChatContentService;
import com.lord.local.gptjavaapi.service.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.springframework.web.servlet.function.ServerResponse.async;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {


    private SseEmitter emitter;

    @Autowired
    private OpenAIUserClient _openAiUserClient;
    @Autowired
    private OpenChatClient _openChatClient;

    @Autowired
    private ChatContentService _chatContentService;
    @Autowired
    private JwtTokenProvider _jwtTokenProvider;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private HttpServletResponse httpServletResponse;

    @GetMapping("/model/{model_id}")
    public String getModel(@PathVariable("model_id") String modelId) {


        OpenAIV1ModelDataResponse model = null;
        try {
            model = _openAiUserClient.getModel(modelId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        return model.toString();
    }

    @GetMapping("/models")
    public OpenAIV1ModelResponse getModels() {
        try {
            OpenAIV1ModelResponse models = _openAiUserClient.getModels();
            return models;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }
    @ApiOperation(value = "对话请求")
    @PostMapping("/completionsV2")
    public ChatBaseResponse<ChatResponseModel> completionsV2(@RequestBody @Valid UserChatContentRequest request) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        // TODO: 2023/4/20 DB存储支持的会话结构
        ChatResponseModel completions = _chatContentService.completions(request.getChatId(), tokenClaims.getUid(), request.getContent());
        return ChatBaseResponse.successResponse(completions);
    }

    @ApiOperation(value = "聊天窗口创建")
    @PostMapping("/session/create")
    public ChatBaseResponse<UserSessionModel> createUserSession(@RequestParam(value = "title", defaultValue = "三体文明研究") @Length(min = 1, max = 255) String title) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        UserSessionModel userSessionModel = _chatContentService.addSession(tokenClaims.getUid(), title);
        return ChatBaseResponse.successResponse(userSessionModel);
    }
    @ApiOperation(value = "聊天列表获取")
    @PostMapping("/session/list")
    public ChatBaseResponse<List<UserSessionModel>> getUserChatSession(){
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        List<UserSessionModel> userChatSession = _chatContentService.getUserChatSession(tokenClaims.getUid());
        return ChatBaseResponse.successResponse(userChatSession);
    }

    @ApiOperation(value = "删除沟通的对话")
    @PostMapping("/session/del")
    public ChatBaseResponse<Boolean> delUserSession(@RequestParam(value = "sessionId", defaultValue = "会话Id") Long sessionId) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        Boolean aBoolean = _chatContentService.delSession(tokenClaims.getUid(), sessionId);
        return ChatBaseResponse.successResponse(aBoolean);
    }

    @ApiOperation(value = "查询对话的具体内容")
    @PostMapping("/content/search")
    public ChatBaseResponse<List<UserChatContentModel>> getChatContent(@RequestParam(value = "sessionId", defaultValue = "会话Id") Long sessionId) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        List<UserChatContentModel> userChatContent = _chatContentService.getUserChatContent(sessionId, tokenClaims.getUid());
        return ChatBaseResponse.successResponse(userChatContent);
    }

//    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    SseEmitter createConnection() {
//        emitter = new SseEmitter();
//        CompletableFuture.runAsync(() -> {
//            sendEvents();
//            sendEvents();
//
//        });
//        return emitter;
//    }

    // in another thread
    @Async
    void sendEvents() {
        try {
            emitter.send("Alpha");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            emitter.send("Omega");


        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }


//    @GetMapping("/")
//    public String home() {
//        return "index";
//    }
//
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public Object sendMessage(@Payload Object chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public Object addUser(@Payload Object chatMessage,
//                               SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username",chatMessage.toString());
//        return chatMessage;
//    }
}
