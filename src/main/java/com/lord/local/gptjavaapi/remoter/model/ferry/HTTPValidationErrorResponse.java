package com.lord.local.gptjavaapi.remoter.model.ferry;

import lombok.Data;

import java.util.List;

@Data
public class HTTPValidationErrorResponse {
    private List<HTTPValidationErrorDetail> detail;
}
