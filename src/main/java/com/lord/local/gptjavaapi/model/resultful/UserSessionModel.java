package com.lord.local.gptjavaapi.model.resultful;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserSessionModel implements Serializable {

    private static final long serialVersionUID = 4358833673588448924L;
    @ApiModelProperty("会话Id")
    private Long chatId;

    /**
     * 会话标题
     */
    @ApiModelProperty("会话标题")
    private String chatTitle;
    /**
     * 会话角色描述
     */
    @ApiModelProperty("会话角色描述")
    private String chatRoleDesc;
    @ApiModelProperty("用户Id")
    private Long userId;

    private Date createTime;

    private Date updateTime;

    private List<UserChatContentModel> userChatContentModels;


}
