package com.lord.local.gptjavaapi.exception;

public enum ResponseStatusEnum {
    CLIENT_NOT_AUTH(-20001, "客户端异常", "无权访问"),
    CLIENT_ILLEGAL_AUTH(-20002, "客户端异常", "非法访问"),
    SYS_DB_ERROR(-10001, "服务数据异常", "数据库访问错误"),
    SYS_DB_ERROR_EDIT(-10002, "服务数据异常", "数据操作异常");
    private Integer code;
    private String name;

    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    ResponseStatusEnum(Integer code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public ResponseStatusEnum parseToEnum(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResponseStatusEnum v : ResponseStatusEnum.values()) {
            if (v.getCode().compareTo(code) == 0) {
                return v;
            }
        }
        return null;
    }
}
