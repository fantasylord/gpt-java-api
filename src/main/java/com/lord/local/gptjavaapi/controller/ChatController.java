package com.lord.local.gptjavaapi.controller;


import com.lord.local.gptjavaapi.model.ChatBaseResponse;
import com.lord.local.gptjavaapi.model.ChatMessageModel;
import com.lord.local.gptjavaapi.model.ChatRequestModel;
import com.lord.local.gptjavaapi.model.ChatResponseModel;
import com.lord.local.gptjavaapi.remoter.client.OpenAIUserClient;
import com.lord.local.gptjavaapi.remoter.client.OpenChatClient;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelDataResponse;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelResponse;
import lombok.extern.slf4j.Slf4j;
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
    private static final Integer contentLength = 10;
    /**
     * 单次聊天最大长度 默认512字符
     */
    @Value("${chat.content.str.length}")
    private static final Integer ChatContentSize = 512;
    /**
     * 单次聊天最大token数量 超过Token长度会刷新上下文 默认512个token
     */
    @Value("${chat.content.token.length}")
    private static final Integer MaxContentToken = 512;
    private Map<String, List<ChatMessageModel>> userConetentMap = new HashMap<>();

    private SseEmitter emitter;

    @Autowired
    private OpenAIUserClient openAiUserClient;
    @Autowired
    private OpenChatClient openChatClient;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @GetMapping("/model/{model_id}")
    public String getModel(@PathVariable("model_id") String modelId) {


        OpenAIV1ModelDataResponse model = null;
        try {
            model = openAiUserClient.getModel(modelId);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
        return model.toString();
    }

    @GetMapping("/models")
    public OpenAIV1ModelResponse getModels() {
        try {
            OpenAIV1ModelResponse models = openAiUserClient.getModels();
            return models;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    @PostMapping("/completions")
    public ChatBaseResponse<ChatResponseModel> completions(@RequestBody String content) {
        if (content != null && content.length() > ChatContentSize) {
            ChatBaseResponse remodel = new ChatBaseResponse<ChatResponseModel>();
            remodel.setInfo("你的B话有点多,服务器不想理你");
            remodel.setCode(-1);
            remodel.setData(null);
            return remodel;
        }
        try {
            ChatRequestModel chatRequestModel = ChatRequestModel.builder()
                    .model("gpt-3.5-turbo-0301")
                    .stream(false)
                    .max_tokens(512)
                    .temperature(0.9)
                    .top_p(1)
                    .messages(new ArrayList<>())
                    .build();

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
                    ChatBaseResponse remodel = ChatBaseResponse.ErrorResponse("你的会话信息过期了", -1000);
                    return remodel;
                }
                userContent = chatList;
                chatRequestModel.getMessages().addAll(chatList);
            } else {
                ChatMessageModel system = setSystemActor();
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
            ChatResponseModel completions = openChatClient.completions(chatRequestModel);
            httpServletResponse.addHeader("ContentSessionUid", contentSessionUid);
            ChatMessageModel assisantAnser = completions.getChoices().get(0).getMessage();
            userContent.add(assisantAnser);
            ChatBaseResponse<ChatResponseModel> remodel = new ChatBaseResponse<>();
            remodel.setData(completions);
            if (completions.getUsage().getTotal_tokens() > MaxContentToken) {
                String restr = remodel.getData().getChoices().get(0).getMessage().getContent().concat("我们聊得够多了");
                remodel.getData().getChoices().get(0).getMessage().setContent(restr);
                userContent.clear();
                userContent.add(setSystemActor());
            }
            //会话续期
            httpServletRequest.getSession().setMaxInactiveInterval(30*60);
            return remodel;
        } catch (Exception e) {
            ChatBaseResponse<ChatResponseModel> remodel = new ChatBaseResponse<>();
            remodel.setInfo("服务器繁忙中");
            return remodel;
        }
    }

    private static ChatMessageModel setSystemActor() {
        ChatMessageModel system = ChatMessageModel.builder()
                .role("system")
                .content("你是一个AI助手,我需要你模拟一名温柔贴心的女朋友来回答我的问题")
                .build();

        return system;
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
