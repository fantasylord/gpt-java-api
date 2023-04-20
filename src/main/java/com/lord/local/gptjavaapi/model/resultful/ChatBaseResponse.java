package com.lord.local.gptjavaapi.model.resultful;

import com.lord.local.gptjavaapi.exception.ResponseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ChatBaseResponse<T> {
    private T data;

    private Integer code;
    private String info;

    private ChatBaseResponse() {

    }

    public static <T> ChatBaseResponse errorResponse(String info, Integer code) {
        ChatBaseResponse remodel = new ChatBaseResponse<>();
        remodel.setInfo(info);
        remodel.setCode(code);
        return remodel;
    }

    public static <T> ChatBaseResponse errorResponse(ResponseStatusEnum enumV) {
        return errorResponse(enumV.getDesc(), enumV.getCode());
    }

    public static <T> ChatBaseResponse successResponse(T data) {
        ChatBaseResponse remodel = new ChatBaseResponse<>();
        remodel.setData(data);
        remodel.setInfo("success");
        remodel.setCode(0);
        return remodel;
    }
}
