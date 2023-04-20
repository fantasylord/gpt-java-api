package com.lord.local.gptjavaapi.model.resultful;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Choices {
    private ChatMessageModel message;
    private String finish_reason;
    private int index;
}
