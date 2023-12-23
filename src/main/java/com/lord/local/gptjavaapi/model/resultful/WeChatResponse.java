package com.lord.local.gptjavaapi.model.resultful;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class WeChatResponse  implements Serializable {
    private static final long serialVersionUID = 3919295452674447552L;
    private int err_code;
    private Map<String,String> data_list;


}
