package com.lord.local.gptjavaapi.dao;

import com.lord.local.gptjavaapi.dao.entity.ChatSession;
import com.lord.local.gptjavaapi.dao.entity.ChatSessionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionDao {
    long countByExample(ChatSessionExample example);

    int deleteByExample(ChatSessionExample example);

    int deleteByPrimaryKey(Long chatId);

    int insert(ChatSession record);

    int insertSelective(ChatSession record);

    List<ChatSession> selectByExample(ChatSessionExample example);

    ChatSession selectByPrimaryKey(Long chatId);

    int updateByExampleSelective(@Param("record") ChatSession record, @Param("example") ChatSessionExample example);

    int updateByExample(@Param("record") ChatSession record, @Param("example") ChatSessionExample example);

    int updateByPrimaryKeySelective(ChatSession record);

    int updateByPrimaryKey(ChatSession record);
}