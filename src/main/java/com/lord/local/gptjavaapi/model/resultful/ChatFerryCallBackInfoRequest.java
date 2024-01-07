package com.lord.local.gptjavaapi.model.resultful;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
public class ChatFerryCallBackInfoRequest implements Serializable {
    private String id;
    private int ts;
    private String sign;
    private int type;
    private String xml;
    private String sender;
    private String roomid;
    private String content;
    private String thumb;
    private String extra;
    private boolean is_at;
    private boolean is_self;
    private boolean is_group;
}
