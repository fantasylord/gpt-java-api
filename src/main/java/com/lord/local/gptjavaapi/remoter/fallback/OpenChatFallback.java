package com.lord.local.gptjavaapi.remoter.fallback;


import com.lord.local.gptjavaapi.model.ChatRequestModel;
import com.lord.local.gptjavaapi.model.ChatResponseModel;
import com.lord.local.gptjavaapi.remoter.client.OpenChatClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenChatFallback implements OpenChatClient {
    @Override
    public ChatResponseModel completions(ChatRequestModel chatRequestModel) {
        log.error("getModel server error ,chatRequestModel:{}",chatRequestModel.toString());
        return new ChatResponseModel();
    }
}
