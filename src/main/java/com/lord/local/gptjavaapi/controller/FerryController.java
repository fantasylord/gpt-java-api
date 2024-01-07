package com.lord.local.gptjavaapi.controller;

import com.alibaba.fastjson2.JSON;
import com.lord.local.gptjavaapi.dao.CallBackInfoDao;
import com.lord.local.gptjavaapi.dao.entity.CallBackInfo;
import com.lord.local.gptjavaapi.model.resultful.ChatFerryCallBackInfoRequest;
import com.lord.local.gptjavaapi.model.resultful.ChatResponseModel;
import com.lord.local.gptjavaapi.remoter.model.ferry.BodySendTextTextPostRequest;
import com.lord.local.gptjavaapi.service.ChatContentService;
import com.lord.local.gptjavaapi.service.ChatFerryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ferry")
@Slf4j
public class FerryController {
    @Autowired
    private CallBackInfoDao callBackInfoDao;

    @Autowired
    private ChatFerryService chatFerryService;

    @Autowired
    private ChatContentService chatContentService;

    @Value("#{'${chat.content.room.white.list:}'.split(',')}")
    private List<String> chatContentRoomWhiteList;
    //    chat.content.room.white.list=44385516018@chatroom
    @Value("#{'${chat.content.sender.black.list:}'.split(',')}")
    private List<String> chatContentSenderBlackList;
//    chat.content.sender.black.list=wxid_6lhivzqx6e7222

    @PostMapping("/callback")
    public String callBack(@RequestBody @Valid ChatFerryCallBackInfoRequest request) {
        try {
            CallBackInfo record = new CallBackInfo();
            record.setCallBackRequest(JSON.toJSONString(request));
            callBackInfoDao.insertSelective(record);
        } catch (Exception e) {
            log.error("回调记录错误");
        }
        //发送者黑名单
        if (chatContentSenderBlackList.isEmpty()
                || chatContentSenderBlackList.stream().anyMatch(i -> StringUtils.compare(i, request.getSender()) == 0)) {
            log.info("接收id:{}的消息,命中SenderBlack消息忽略!", request.getId());
            return "true";
        }
        //回复口令判断
        if (!isRightContentKey(request)) {
            return "true";
        }

        if (StringUtils.isEmpty(request.getRoomid()) && StringUtils.isEmpty(request.getSender())) {
            log.info("接受id:{}的消息,未知的发送源头", request.getId());
            return "true";
        }
        log.info("接收id:{}的消息,准备处理消息!", request.getId());
        //处理私信逻辑
        if (StringUtils.isNotEmpty(request.getSender())) {

            boolean result = privateMSGProgram(request);
            return String.valueOf(result);

        }
        //处理群消息逻辑
        else if (StringUtils.isNotEmpty(request.getRoomid())) {

            boolean result = roomMSGProgram(request);
            return String.valueOf(result);
        }


//        BodySendTextTextPostRequest msg = new BodySendTextTextPostRequest();
//        if(StringUtils.isNotEmpty(request.getRoomid()))
//        {
//            msg.setReceiver(request.getRoomid());
//        }
//        else {
//            msg.setReceiver(request.getSender());
//        }
//
//        String msgContent="";
//        ChatResponseModel completions = chatContentService.completions(15L, 1L, request.getContent());
//        try {
//            msgContent = completions.getChoices().get(0).getMessage().getContent();
//        } catch (NullPointerException e) {
//            msgContent = "宕机了,待会再试试";
//        }
//        msg.setMsg(msgContent);
//        boolean result = chatFerryService.sendMsg(msg);

        return "true";
    }

//    private boolean getWhiteFilterResult(ChatFerryCallBackInfoRequest request) {
//        //消息群白名单 判断群组是否在白名单
//        if (StringUtils.isNotEmpty(request.getRoomid())) {
//            if (chatContentRoomWhiteList.isEmpty()
//                    || chatContentRoomWhiteList.stream().noneMatch(i -> StringUtils.compare(i, request.getRoomid()) == 0)) {
//                log.info("接收id:{}的消息,不在roomWhite消息忽略!", request.getId());
//                return false;
//
//            }
//            return true;
//        }
//        //私信白名单 判断发送人是否在白名单
//        else if (StringUtils.isNotEmpty(request.getSender())) {
//            if (chatContentRoomWhiteList.isEmpty()
//                    || chatContentRoomWhiteList.stream().noneMatch(i -> StringUtils.compare(i, request.getSender()) == 0)) {
//                log.info("接收id:{}的消息,不在senderWhite消息忽略!", request.getId());
//                return false;
//
//            }
//            return true;
//        }
//        return false;
//    }

    private boolean privateMSGProgram(ChatFerryCallBackInfoRequest request) {

        if (StringUtils.isNotEmpty(request.getSender())) {
            if (chatContentRoomWhiteList.isEmpty()
                    || chatContentRoomWhiteList.stream().noneMatch(i -> StringUtils.compare(i, request.getSender()) == 0)) {
                log.info("接收id:{}的消息,不在senderWhite消息忽略!", request.getId());
                return false;
            }
        }
        if (request.getContent().indexOf("配置菜单") > 0) {
            return adminMSGProgram(request);
        }
        //发送消息
        return sendToGptchatMsg(request, request.getSender());
    }

    private boolean roomMSGProgram(ChatFerryCallBackInfoRequest request) {
        if (StringUtils.isEmpty(request.getRoomid())) {
            return false;
        }
        if (StringUtils.isNotEmpty(request.getRoomid())) {
            if (chatContentRoomWhiteList.isEmpty()
                    || chatContentRoomWhiteList.stream().noneMatch(i -> StringUtils.compare(i, request.getRoomid()) == 0)) {
                log.info("接收id:{}的消息,不在roomWhite消息忽略!", request.getId());
                return false;
            }
        }
        //发送消息
        return sendToGptchatMsg(request, request.getRoomid());
    }

    private boolean sendToGptchatMsg(ChatFerryCallBackInfoRequest request, String sendId) {
        String msgContent = "";
        ChatResponseModel completions = chatContentService.completionsByWeChat(request.getContent());
        try {
            msgContent = completions.getChoices().get(0).getMessage().getContent();
        } catch (NullPointerException e) {
            msgContent = "宕机了,待会再试试";
        }
        boolean replyResultSuccess = replyMsg(request, sendId, msgContent);
        if (!replyResultSuccess) {
            return false;
        }
        return true;
    }

    private boolean replyMsg(ChatFerryCallBackInfoRequest request, String sendId, String msgContent) {
        BodySendTextTextPostRequest msg = new BodySendTextTextPostRequest();
        msg.setReceiver(sendId);
        msg.setMsg(msgContent);
        boolean result = chatFerryService.sendMsg(msg);
        if (!result) {
            log.warn("接收id:{}的消息,gpt聊天服务访问错误!,msg:{}", request.getId(), msg);
            return false;
        }
        return true;
    }

    private boolean adminMSGProgram(ChatFerryCallBackInfoRequest request) {
        String adminWhiteList = "wxid_5ga60fg63wmb22";
        if (adminWhiteList.indexOf(request.getSender()) < 0) {
            return false;
        }
        if (StringUtils.isEmpty(request.getContent())
                || (request.getContent().indexOf("可莉在吗") < 0
                && request.getContent().indexOf("@骑士团-可莉") < 0)) {
            log.info("接收id:{}的消息,无口令消息忽略!", request.getId());
            return false;
        }
        if (request.getContent().indexOf("1+admin") == 0) {
            return replyMsg(request, request.getSender(), "开发中");
        } else if (request.getContent().indexOf("2+admin") == 0) {
            return replyMsg(request, request.getSender(), "开发中");

        } else if (request.getContent().indexOf("3+admin") == 0) {
            return replyMsg(request, request.getSender(), "开发中");

        } else if (request.getContent().indexOf("4+admin") == 0) {
            return replyMsg(request, request.getSender(), "开发中");

        }
        return replyMsg(request, request.getSender(), getAdminCommandList());
    }

    private static Map<Integer, String> getCommandMap() {
        Map comandMap = new HashMap<Integer, String>();
        comandMap.put(1, "1.执行bat");
        comandMap.put(2, "2.执行定时任务");
        comandMap.put(3, "3.执行系统重起");
        return comandMap;
    }

    private String getAdminCommandList() {

//        comandMap.put(1,"");
        String commands = "";
        Map<Integer, String> commandMap = getCommandMap();
        for (String item :
                commandMap.values()) {
            commands += item + "\n";
        }
        commands += "回复格式： 命令编号+admin@管理员动态密码。例如 1+admin@1234";
        return commands;
    }

    private boolean isRightContentKey(ChatFerryCallBackInfoRequest request) {
        if (StringUtils.isEmpty(request.getContent())
                || (request.getContent().indexOf("可莉在吗") < 0
                && request.getContent().indexOf("@骑士团-可莉") < 0)) {
            log.info("接收id:{}的消息,无口令消息忽略!", request.getId());
            return false;
        }
        return true;
    }


}
