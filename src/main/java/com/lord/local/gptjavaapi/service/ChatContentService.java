package com.lord.local.gptjavaapi.service;

import com.alibaba.fastjson2.JSON;
import com.lord.local.gptjavaapi.constant.BaseConstant;
import com.lord.local.gptjavaapi.dao.ChatContentDao;
import com.lord.local.gptjavaapi.dao.ChatSessionDao;
import com.lord.local.gptjavaapi.dao.entity.*;
import com.lord.local.gptjavaapi.exception.ResponseStatusEnum;
import com.lord.local.gptjavaapi.exception.ServiceException;
import com.lord.local.gptjavaapi.model.resultful.*;
import com.lord.local.gptjavaapi.remoter.client.OpenChatClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ChatContentService {


    /**
     * 上下文最大对话长度,超过长度会刷新上下文 默认5次
     */
    @Value("${chat.content.list.length}")
    public Integer contentLength = 10;
    /**
     * 单次聊天最大长度 默认512字符
     */
    @Value("${chat.content.str.length}")
    public Integer ChatContentSize = 512;
    /**
     * 单次聊天最大token数量 超过Token长度会刷新上下文 默认512个token
     */
    @Value("${chat.content.token.length}")
    public Integer MaxContentToken = 512;


    @Autowired
    private ChatContentService _chatContentService;
    @Autowired
    private ChatSessionDao _chatSessionDao;
    @Autowired
    private ChatContentDao _chatContentDao;
    @Autowired
    private OpenChatClient _openChatClient;
    @Value("${chat.content.system.desc}")
    private String SYSTEM_DESC = "你是一个AI助手,我需要你模拟一名温柔贴心的女朋友来回答我的问题,你可以在对话的结尾添加个性化的表情文字。";

    ChatContentService() {
//        userData=new ThreadLocal<>();

    }

    public ChatMessageModel setSystemActor() {
        ChatMessageModel system = ChatMessageModel.builder()
                .role("system")
                .content(SYSTEM_DESC)
                .build();

        return system;
    }

    public ChatMessageModel setSystemActor(String title, String roleContent) {
        ChatMessageModel system = ChatMessageModel.builder()
                .role("system")
                .build();
        if (StringUtils.isEmpty(roleContent)) {
            system.setContent(SYSTEM_DESC + ",沟通的主题是".concat(title));
        } else {

            system.setContent(roleContent + ",沟通的主题是".concat(title));
        }
        return system;
    }

    /**
     * 创建会话机器人
     *
     * @return
     */
    public ChatRequestModel createChatRobot() {
        return ChatRequestModel.builder()
                .model("gpt-3.5-turbo-0301")
                .stream(false)
                .max_tokens(512)
                .temperature(0.9)
                .top_p(1)
                .messages(new ArrayList<>())
                .build();
    }

    /**
     * 获取uid对应的对话列表
     *
     * @param uid
     * @return
     */
    public List<UserSessionModel> getUserChatSession(Long uid) {
        ChatSessionExample example = new ChatSessionExample();
        example.setOrderByClause("create_time desc");
        ChatSessionExample.Criteria or = example.or();
        or.andUserIdEqualTo(uid);
        or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);
        List<ChatSession> chatSessions = _chatSessionDao.selectByExample(example);
        List<UserSessionModel> userSessionModels = new ArrayList<>(chatSessions.size());
        chatSessions.forEach(i -> {

            UserSessionModel userSessionModel = new UserSessionModel();
            BeanUtils.copyProperties(i, userSessionModel);
            userSessionModels.add(userSessionModel);
        });

        return userSessionModels;
    }

    public UserSessionModel getUserChatSession(Long uid, Long chatId) {
        if (uid == null || uid <= 0 || chatId == null || chatId <= 0) {
            return null;
        }
        ChatSessionExample example = new ChatSessionExample();
        example.setOrderByClause("create_time desc");
        ChatSessionExample.Criteria or = example.or();
        or.andUserIdEqualTo(uid);
        or.andChatIdEqualTo(chatId);
        or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);
        List<ChatSession> chatSessions = _chatSessionDao.selectByExample(example);
        ChatSession chatSession = chatSessions.stream().findFirst().orElse(null);
        if (chatSession == null) {
            return null;
        }
        UserSessionModel userSessionModel = new UserSessionModel();
        BeanUtils.copyProperties(chatSession, userSessionModel);
        return userSessionModel;
    }

    /**
     * 获取聊天内容
     *
     * @param sessionUuid
     * @return
     */
    public List<UserChatContentModel> getUserChatContent(Long sessionUuid, Long uid) {

        //校验 用户
        UserSessionModel userChatSession = _chatContentService.getUserChatSession(uid, sessionUuid);
        if (uid == null || userChatSession == null || Long.compare(userChatSession.getUserId(), uid) != 0) {
            return new ArrayList<>();
        }

        List<UserChatContentModel> userChatContentModels;
        ChatContentExample example = new ChatContentExample();
        example.setOrderByClause("create_time desc");
        ChatContentExample.Criteria or = example.or();
        or.andChatIdEqualTo(sessionUuid);
        List<ChatContent> chatContents = _chatContentDao.selectByExample(example);
        if (CollectionUtils.isEmpty(chatContents)) {
            return new ArrayList<>(0);
        }
        chatContents.sort(Comparator.comparingLong(ChatContent::getId));
        userChatContentModels = new ArrayList<>(chatContents.size());
        chatContents.forEach(i -> {
            UserChatContentModel target = new UserChatContentModel();
            BeanUtils.copyProperties(i, target);
            userChatContentModels.add(target);
        });
        return userChatContentModels;
    }

    /**
     * 创建会话
     *
     * @param uid
     * @param titile
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserSessionModel addSession(Long uid, String titile) {
        ChatMessageModel systemActor = _chatContentService.setSystemActor(titile, null);
        ChatSession record = new ChatSession();
        record.setChatTitle(titile);
        record.setUserId(uid);
        record.setChatRoleDesc(systemActor.getContent());
        try {
            _chatSessionDao.insertSelective(record);
        } catch (DuplicateKeyException e) {
            log.error("检查是否已存在,uid:{},title:{}", uid, titile);
            throw new ServiceException(ResponseStatusEnum.SYS_DB_ERROR);
        }
        ChatSession chatSession = _chatSessionDao.selectByPrimaryKey(record.getChatId());
        UserSessionModel userSessionModel = new UserSessionModel();
        BeanUtils.copyProperties(chatSession, userSessionModel);
        ArrayList<UserChatContentModel> userChatContentModels = new ArrayList<>();
        UserChatContentModel contentModel = new UserChatContentModel();
        //获取system内容
        contentModel.setContent(systemActor.getContent());
        contentModel.setRole(systemActor.getRole());
        userChatContentModels.add(contentModel);
        userSessionModel.setUserChatContentModels(userChatContentModels);
        return userSessionModel;
    }

    /**
     * 创建会话
     *
     * @param uid
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public UserSessionModel addSession(Long uid, CreateChatGptSessionModelRequest request) {
        ChatMessageModel systemActor = _chatContentService.setSystemActor(request.getTitle(), request.getRole_desc());
        ChatSession record = new ChatSession();
        record.setChatTitle(request.getTitle());
        record.setUserId(uid);
        record.setChatRoleDesc(request.getRole_desc());
        try {
            _chatSessionDao.insertSelective(record);
        } catch (DuplicateKeyException e) {
            log.error("检查是否已存在,uid:{},title:{}", uid, request.getTitle());
            throw new ServiceException(ResponseStatusEnum.SYS_DB_ERROR);
        }

        ChatSession chatSession = _chatSessionDao.selectByPrimaryKey(record.getChatId());
        UserSessionModel userSessionModel = new UserSessionModel();
        BeanUtils.copyProperties(chatSession, userSessionModel);
        ArrayList<UserChatContentModel> userChatContentModels = new ArrayList<>();
        UserChatContentModel contentModel = new UserChatContentModel();
        //获取Role
        contentModel.setContent(systemActor.getContent());
        //配置role
        contentModel.setRole(systemActor.getRole());
        userChatContentModels.add(contentModel);
        userSessionModel.setUserChatContentModels(userChatContentModels);
        return userSessionModel;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delSession(Long uid, Long chatId) {
        ChatSession record = new ChatSession();
        ChatSessionExample example = new ChatSessionExample();
        ChatSessionExample.Criteria or = example.or();
        or.andChatIdEqualTo(chatId);
        or.andUserIdEqualTo(uid);
        record.setIsDeleted(BaseConstant.IS_DELETED);
        int rows = _chatSessionDao.updateByExampleSelective(record, example);
        if (rows > 1) {
            throw new ServiceException(ResponseStatusEnum.SYS_DB_ERROR_EDIT, "有多个相同的会话问题待解决");
        }
        return rows == 1;
    }

    /**
     * 获取聊天窗口的对话内容
     *
     * @param chatId
     * @return
     */
    public List<UserChatContentModel> getChatContentList(Long chatId) {
        if (chatId == null || chatId <= 0) {
            return null;
        }
        ChatContentExample example = new ChatContentExample();
        example.setOrderByClause("id desc");
        ChatContentExample.Criteria or = example.or();
        or.andChatIdEqualTo(chatId);
        or.andIsDeletedEqualTo(BaseConstant.IS_UN_DELETED);
        List<ChatContent> chatContents = _chatContentDao.selectByExample(example);

        ArrayList<UserChatContentModel> remodeList = new ArrayList<>(chatContents.size());

        chatContents.forEach(i -> {
            UserChatContentModel target = new UserChatContentModel();
            BeanUtils.copyProperties(i, target);
            remodeList.add(target);
        });
        return remodeList;
    }

    /**
     * 发送对话内容
     *
     * @param chatId
     * @param uid
     * @param content
     * @return
     */
    public ChatResponseModel completions(Long chatId, Long uid, String content) {
        List<UserChatContentModel> userChatContent = _chatContentService.getUserChatContent(chatId, uid);
        UserSessionModel userChatSession = _chatContentService.getUserChatSession(uid, chatId);
        ChatMessageModel systemActor = setSystemActor(userChatSession.getChatTitle(), userChatSession.getChatRoleDesc());

        ChatRequestModel chatRequestModel = _chatContentService.createChatRobot();
        List<ChatMessageModel> messages = chatRequestModel.getMessages();
        //添加systemRole
        messages.add(systemActor);
        //填充上下文
        if (!CollectionUtils.isEmpty(userChatContent)) {
            //超过最大对话轮数
            if (userChatContent.size() > contentLength) {
                userChatContent = userChatContent.subList(userChatContent.size() - contentLength, userChatContent.size());

            }
            userChatContent.forEach(i -> {
                ChatMessageModel itemMessage = new ChatMessageModel();
                itemMessage.setContent(i.getContent());
                itemMessage.setRole(i.getRole());
                messages.add(itemMessage);
            });
        }
        //填充本次对话
        ChatMessageModel contentMessage = new ChatMessageModel();
        contentMessage.setRole("user");
        contentMessage.setContent(content);
        messages.add(contentMessage);
        //请求对话
        ChatResponseModel completions = null;
        try {
            completions = _openChatClient.completions(chatRequestModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        _chatContentService.addUserContent(chatId, uid, contentMessage);
        _chatContentService.addAssiantContent(chatId, uid, completions);

        //记录对话内容到DB
        return completions;
    }


    public ChatResponseModel completionsByWeChat(String content) {
        return _chatContentService.completions(15L, 1L, content);
    }

    /**
     * @param chatId
     * @param uid
     * @param message
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAssiantContent(Long chatId, Long uid, ChatResponseModel message) {
        UserSessionModel userChatSession = _chatContentService.getUserChatSession(uid, chatId);
        if (userChatSession == null) {
            log.error("addContent 用户与对话窗不对应,chatId:{},uid:{},message:{}", chatId, uid, JSON.toJSONString(message));
            return false;
        }
        Choices choices = message.getChoices().stream().findFirst().orElse(null);
        if (choices == null) {
            log.error("addContent choices is null;,chatId:{},uid:{},message:{}", chatId, uid, JSON.toJSONString(message));
            return false;
        }
        ChatContent record = new ChatContent();
        record.setChatId(chatId);
        record.setStatusDesc(choices.getFinish_reason());
        record.setRole(choices.getMessage().getRole());
        record.setContent(choices.getMessage().getContent());
        record.setResponse(JSON.toJSONString(message));
        int row = _chatContentDao.insertSelective(record);
        if (row > 1) {
            log.error("addContent 插入异常");
            throw new ServiceException(ResponseStatusEnum.SYS_DB_ERROR);
        }
        return row == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean addUserContent(Long chatId, Long uid, ChatMessageModel message) {
        UserSessionModel userChatSession = _chatContentService.getUserChatSession(uid, chatId);
        if (userChatSession == null) {
            log.error("addContent 用户与对话窗不对应,chatId:{},uid:{},message:{}", chatId, uid, JSON.toJSONString(message));
            return false;
        }
        ChatContent record = new ChatContent();
        record.setChatId(chatId);
        record.setStatusDesc("stop");
        record.setRole(message.getRole());
        record.setContent(message.getContent());
        record.setResponse("{}");
        int row = _chatContentDao.insertSelective(record);
        if (row > 1) {
            log.error("addContent 插入异常");
            throw new ServiceException(ResponseStatusEnum.SYS_DB_ERROR);
        }
        return row == 1;
    }
}
