package com.lord.local.gptjavaapi.remoter.client;

import com.lord.local.gptjavaapi.config.OpenAiChatFeignConfig;
import com.lord.local.gptjavaapi.model.resultful.ChatRequestModel;
import com.lord.local.gptjavaapi.model.resultful.ChatResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "openai", url = "https://api.openai.com",contextId ="OpenChatUserClient", configuration = OpenAiChatFeignConfig.class)
public interface OpenChatClient {
    @PostMapping("/v1/chat/completions")
    ChatResponseModel completions(@RequestBody ChatRequestModel chatRequestModel);

}
