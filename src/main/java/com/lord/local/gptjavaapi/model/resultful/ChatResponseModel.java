package com.lord.local.gptjavaapi.model.resultful;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseModel {

    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choices> choices;
}
