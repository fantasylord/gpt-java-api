package com.lord.local.gptjavaapi.remoter.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIV1ModelDataResponse {
    private String id;
    private String object;
    private long created;
    private String owned_by;
    private List<OpenAIV1ModelResponseDataPermission> permission;
    private String root;
    private String parent;
}
