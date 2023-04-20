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
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.springframework.web.servlet.function.ServerResponse.async;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    /**
     * 上下文最大对话长度,超过长度会刷新上下文 默认5次
     */
    @Value("${chat.content.list.length}")
    public   Integer contentLength = 10;
    /**
     * 单次聊天最大长度 默认512字符
     */
    @Value("${chat.content.str.length}")
    public   Integer ChatContentSize = 512;
    /**
     * 单次聊天最大token数量 超过Token长度会刷新上下文 默认512个token
     */
    @Value("${chat.content.token.length}")
    public  Integer MaxContentToken = 512;
    private Map<String, List<ChatMessageModel>> userConetentMap = new HashMap<>();

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

    @ApiOperation(value = "session模式的会话聊天", notes = "服务器会返回session字段作为上下文,有效期30分钟,不做持久化记录")
    @PostMapping("/completions")
    public ChatBaseResponse<ChatResponseModel> completions(@RequestBody String content) {
        if (content != null && content.length() > ChatContentSize) {
            ChatBaseResponse remodel = ChatBaseResponse.errorResponse("你的B话有点多,服务器不想理你", -1);
            return remodel;
        }
        try {
            ChatRequestModel chatRequestModel = _chatContentService.createChatRobot();
            String contentSessionUid = httpServletRequest.getHeader("ContentSessionUid");
            List<ChatMessageModel> chatList = new ArrayList<>(6);
            List<ChatMessageModel> userContent;
            if (contentSessionUid != null) {
                List<ChatMessageModel> choices = userConetentMap.get(contentSessionUid);
                if (choices != null && choices.size() > 0) {
                    int size = choices.size();
                    if (size > contentLength) {
                        chatList = choices.subList(size - contentLength, size);
                    } else {
                        chatList = choices;
                    }
                } else {
                    ChatBaseResponse remodel = ChatBaseResponse.errorResponse("你的会话信息过期了", -1000);
                    return remodel;
                }
                userContent = chatList;
                chatRequestModel.getMessages().addAll(chatList);
            } else {
                ChatMessageModel system = _chatContentService.setSystemActor();
                chatRequestModel.getMessages().add(system);
                UUID uuid = UUID.randomUUID();
                httpServletRequest.getSession().setAttribute("ContentSessionUid", uuid.toString());
                contentSessionUid = uuid.toString();
                userConetentMap.put(uuid.toString(), new ArrayList<>());
                userContent = userConetentMap.get(uuid.toString());
                userContent.add(system);
            }
            ChatMessageModel message = ChatMessageModel.builder()
                    .role("user")
                    .content(content)
                    .build();
            chatRequestModel.getMessages()
                    .add(message);
            userContent.add(message);
            ChatResponseModel completions = _openChatClient.completions(chatRequestModel);
            httpServletResponse.addHeader("ContentSessionUid", contentSessionUid);
            ChatMessageModel assisantAnser = completions.getChoices().get(0).getMessage();
            userContent.add(assisantAnser);
            ChatBaseResponse<ChatResponseModel> remodel = ChatBaseResponse.successResponse(completions);
            if (completions.getUsage().getTotal_tokens() > MaxContentToken) {
                String restr = remodel.getData().getChoices().get(0).getMessage().getContent().concat("我们聊得够多了");
                remodel.getData().getChoices().get(0).getMessage().setContent(restr);
                userContent.clear();
                userContent.add(_chatContentService.setSystemActor());
            }
            //会话续期
            httpServletRequest.getSession().setMaxInactiveInterval(30 * 60);
            return remodel;
        } catch (Exception e) {
            ChatBaseResponse<ChatResponseModel> remodel = ChatBaseResponse.errorResponse("服务器繁忙中", -1000);
            return remodel;
        }
    }


    @PostMapping("/completionsV2")
    public ChatBaseResponse<ChatResponseModel> completionsV2(@RequestBody UserChatContentRequest request) {
        if (request == null || request.getContent() != null && request.getContent().length() > ChatContentSize) {
            ChatBaseResponse remodel = ChatBaseResponse.errorResponse("你的B话有点多,服务器不想理你", -1);
            return remodel;
        }
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        // TODO: 2023/4/20 DB存储支持的会话结构
        ChatResponseModel completions = _chatContentService.completions(request.getChatId(), tokenClaims.getUid(), request.getContent());
        return ChatBaseResponse.successResponse(completions);
    }

    @ApiOperation(value = "DB存储的持久化聊天创建", notes = "服务器会记录用户的聊天上下文,这里用于创建聊天窗口")
    @PostMapping("/createSession")
    public ChatBaseResponse<UserSessionModel> createUserSession(@RequestParam(value = "title", defaultValue = "三体文明研究") @Length(min = 1, max = 255) String title) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        UserSessionModel userSessionModel = _chatContentService.addSession(tokenClaims.getUid(), title);
        return ChatBaseResponse.successResponse(userSessionModel);
    }

    @ApiOperation(value = "删除沟通的对话")
    @PostMapping("/delUserSession")
    public ChatBaseResponse<Boolean> delUserSession(@RequestParam(value = "sessionId", defaultValue = "会话Id") Long sessionId) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        Boolean aBoolean = _chatContentService.delSession(tokenClaims.getUid(), sessionId);
        return ChatBaseResponse.successResponse(aBoolean);
    }

    @ApiOperation(value = "查询对话的具体内容")
    @PostMapping
    public ChatBaseResponse<List<UserChatContentModel>> getChatContent(@RequestParam(value = "sessionId", defaultValue = "会话Id") Long sessionId) {
        User tokenClaims = _jwtTokenProvider.getTokenClaims(httpServletRequest);
        List<UserChatContentModel> userChatContent = _chatContentService.getUserChatContent(sessionId, tokenClaims.getUid());
        return ChatBaseResponse.successResponse(userChatContent);
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter createConnection() {
        emitter = new SseEmitter();
        CompletableFuture.runAsync(() -> {
            sendEvents();
            sendEvents();

        });
        return emitter;
    }

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

}
