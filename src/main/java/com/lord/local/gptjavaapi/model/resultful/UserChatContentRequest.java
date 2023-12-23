package com.lord.local.gptjavaapi.model.resultful;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Map;

@Data
@Valid
public class UserChatContentRequest implements Serializable {
    private static final long serialVersionUID = 611603148340678597L;

    @ApiModelProperty(name = "发送的内容", example = "喝水会不会狗带", required = true)
    @Length(min = 1,max = 512,message = "你的B话有点多,服务器不想理你")
    private String content;
    @ApiModelProperty(name = "会话Id", example = "2233", required = true)
    private Long chatId;

    private String weChatKey;
}
