package com.lord.local.gptjavaapi.exception;

public class ServiceException extends RuntimeException {
    private ResponseStatusEnum responseStatusEnum;


    private String serviceCustomMesssage;

    public ServiceException(ResponseStatusEnum responseStatusEnum) {

        super("异常状态码为：" + responseStatusEnum.getCode()
                + "；具体异常信息为：" + responseStatusEnum.getDesc());
        this.responseStatusEnum = responseStatusEnum;
    }

    public ServiceException(ResponseStatusEnum responseStatusEnum, String serviceCustomMesssage) {

        super("异常状态码为：" + responseStatusEnum.getCode()
                + "；具体异常信息为：" + responseStatusEnum.getDesc()
                + "补充异常:" + serviceCustomMesssage);
        this.responseStatusEnum = responseStatusEnum;
    }

    @Override
    public String getMessage() {
        String message = this.getMessage();
        return message.concat(String.valueOf(serviceCustomMesssage));
    }

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }
}
