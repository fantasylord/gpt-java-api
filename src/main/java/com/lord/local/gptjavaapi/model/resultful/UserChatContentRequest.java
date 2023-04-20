package com.lord.local.gptjavaapi.model.resultful;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserChatContentRequest implements Serializable {
    private static final long serialVersionUID = 611603148340678597L;

    @ApiModelProperty(name = "发送的内容", example = "喝水会不会狗带", required = true)
    private String content;
    @ApiModelProperty(name = "会话Id", example = "2233", required = true)
    private Long chatId;
}
