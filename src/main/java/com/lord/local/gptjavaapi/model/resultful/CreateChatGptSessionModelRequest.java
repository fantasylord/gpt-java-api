package com.lord.local.gptjavaapi.model.resultful;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

@Data
public class CreateChatGptSessionModelRequest implements Serializable {

    @ApiModelProperty(value = "title", example = "三体文明研究")
    @Length(min = 1, max = 255)
    private String title;
    @ApiModelProperty(value = "role_desc", example = "你是一名AI助手")
    @Length(max = 1024)
    private String role_desc;

    @ApiModelProperty(value = "tag", example = "微信群XXXX")
    @Length(max = 255)
    private String tag;

}
