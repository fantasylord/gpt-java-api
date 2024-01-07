package com.lord.local.gptjavaapi.service;

import com.lord.local.gptjavaapi.remoter.client.ferry.ChatFerryHttpClient;
import com.lord.local.gptjavaapi.remoter.model.ferry.BodySendTextTextPostRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatFerryService {

    @Autowired
    private ChatFerryHttpClient chatFerryHttpClient;

    /**
     * 发送文本
     * @return
     */
    public boolean sendMsg(BodySendTextTextPostRequest msg){
        try {
            ResponseEntity<Object> objectResponseEntity = chatFerryHttpClient.sendTextTextPost(msg);
        } catch (Exception e) {
           log.error("ChatFerryService.sendMsgError,req:{},msg:{}",msg,e.getMessage(),e);
           return false;
        }

        return true;
    }
    public String getRoomId(){
//        chatFerryHttpClient.get
        return "";
    }


}
