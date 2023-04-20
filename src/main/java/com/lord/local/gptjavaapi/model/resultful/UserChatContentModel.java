package com.lord.local.gptjavaapi.model.resultful;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserChatContentModel {
    @ApiModelProperty("内容序号")
    private Long id;

    @ApiModelProperty(name = "发送的内容",example = "喝水会不会狗带")
    private String content;

    @ApiModelProperty(name = "角色 Assistant,user",example = "user")
    private String role;

    private Date createTime;

    private Date updateTime;
    @ApiModelProperty(name = "用来描述当前对话是否完结 stop 已完结，其他则未完结",value = "stop")
    private String statusDesc;

}
