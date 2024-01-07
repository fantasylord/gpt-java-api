package com.lord.local.gptjavaapi.remoter.model.ferry;

import lombok.Data;

import java.io.Serializable;

@Data
public class BodySendPatMsgPatPostRequest implements Serializable {
    private String roomid;
    private String wxid;

}
