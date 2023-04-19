package com.lord.local.gptjavaapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestModel {
    private String model;
    private boolean stream;
    private double temperature;
    private double top_p;
    private List<ChatMessageModel> messages;
    private int max_tokens;


}
