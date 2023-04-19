package com.lord.local.gptjavaapi.remoter.fallback;

import com.lord.local.gptjavaapi.remoter.client.OpenAIUserClient;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelDataResponse;
import com.lord.local.gptjavaapi.remoter.model.OpenAIV1ModelResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 */
@Slf4j
public class OpenAIUserFallback implements OpenAIUserClient {
    @Override
    public OpenAIV1ModelDataResponse getModel(String modelId) {
        log.error("getModel server error ,id:{}",modelId);
        return new OpenAIV1ModelDataResponse();
    }
    @Override
    public OpenAIV1ModelResponse getModels() {
        log.error("getModel server error ");
        return new OpenAIV1ModelResponse();
    }


}
