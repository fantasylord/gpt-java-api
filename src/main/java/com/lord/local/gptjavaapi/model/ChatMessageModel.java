package com.lord.local.gptjavaapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageModel {
    /**
     对话的2个人 机器和你
     */
    private String role;
    //发出的内容
    /**
     对话发出的内容
     */
    private String content;

}
