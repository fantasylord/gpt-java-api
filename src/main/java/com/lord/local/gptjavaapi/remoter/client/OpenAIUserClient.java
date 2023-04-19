package com.lord.local.gptjavaapi.remoter.client;

import com.lord.local.gptjavaapi.config.OpenAiChatFeignConfig;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelDataResponse;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "openai", url = "https://api.openai.com",contextId = "OpenAIUserClient",configuration = OpenAiChatFeignConfig.class)
public interface OpenAIUserClient {

    /**
     * https://api.openai.com/v1/models/babbage
     * @param modelId
     * @return
     */
    @GetMapping("/v1/models/{model_id}")
    OpenAIV1ModelDataResponse getModel(@PathVariable("model_id") String modelId);
    @GetMapping("/v1/models")
    OpenAIV1ModelResponse getModels();


//    @Headers("Authorization: Bearer {accessToken}")
//    @PostMapping("/v1/models")
//    OpenAICompletionResponse complete(@PathVariable("engineId") String engineId, @RequestBody OpenAICompletionRequest request, @Param("accessToken") String accessToken);
}


