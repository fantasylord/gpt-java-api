package com.lord.local.gptjavaapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBaseResponse<T>{
    private T data;

    private Integer code;
    private String info;


    public static ChatBaseResponse ErrorResponse(String info, Integer code){
        ChatBaseResponse remodel = new ChatBaseResponse<>();
        remodel.setInfo(info);
        remodel.setCode(code);
        return remodel;
    }
}
