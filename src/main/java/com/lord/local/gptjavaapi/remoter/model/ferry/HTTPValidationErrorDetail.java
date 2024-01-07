package com.lord.local.gptjavaapi.remoter.model.ferry;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HTTPValidationErrorDetail  implements Serializable {
    private static final long serialVersionUID = 6065035807205291853L;
    private Date loc;
    private String msg;
    private String type;
}
