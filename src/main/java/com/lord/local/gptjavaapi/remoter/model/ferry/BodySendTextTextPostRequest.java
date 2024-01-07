package com.lord.local.gptjavaapi.remoter.model.ferry;

import lombok.Data;

import java.io.Serializable;

@Data
public class BodySendTextTextPostRequest implements Serializable {
    private String msg;
    private String receiver;
    private String aters;
}
