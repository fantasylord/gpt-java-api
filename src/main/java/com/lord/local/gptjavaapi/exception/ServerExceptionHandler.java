package com.lord.local.gptjavaapi.exception;

import com.lord.local.gptjavaapi.model.resultful.ChatBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ServerExceptionHandler {

    /**
     * 只要抛出ServiceException，就会被此方法拦截到，随后可以自定义处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ChatBaseResponse<?> returnMyException(ServiceException e) {
        log.error("server error msg:{}", e.getMessage(), e);
        e.printStackTrace();
        return ChatBaseResponse.errorResponse(e.getMessage(), e.getResponseStatusEnum().getCode());
    }
}
