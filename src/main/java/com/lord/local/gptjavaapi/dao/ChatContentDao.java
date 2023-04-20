package com.lord.local.gptjavaapi.dao;

import com.lord.local.gptjavaapi.dao.entity.ChatContent;
import com.lord.local.gptjavaapi.dao.entity.ChatContentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatContentDao {
    long countByExample(ChatContentExample example);

    int deleteByExample(ChatContentExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChatContent record);

    int insertSelective(ChatContent record);

    List<ChatContent> selectByExampleWithBLOBs(ChatContentExample example);

    List<ChatContent> selectByExample(ChatContentExample example);

    ChatContent selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChatContent record, @Param("example") ChatContentExample example);

    int updateByExampleWithBLOBs(@Param("record") ChatContent record, @Param("example") ChatContentExample example);

    int updateByExample(@Param("record") ChatContent record, @Param("example") ChatContentExample example);

    int updateByPrimaryKeySelective(ChatContent record);

    int updateByPrimaryKeyWithBLOBs(ChatContent record);

    int updateByPrimaryKey(ChatContent record);
}