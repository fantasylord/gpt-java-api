package com.lord.local.gptjavaapi.dao;

import com.lord.local.gptjavaapi.dao.entity.CallBackInfo;
import com.lord.local.gptjavaapi.dao.entity.CallBackInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CallBackInfoDao {
    long countByExample(CallBackInfoExample example);

    int deleteByExample(CallBackInfoExample example);

    int deleteByPrimaryKey(Integer callBackId);

    int insert(CallBackInfo record);

    int insertSelective(CallBackInfo record);

    List<CallBackInfo> selectByExampleWithBLOBs(CallBackInfoExample example);

    List<CallBackInfo> selectByExample(CallBackInfoExample example);

    CallBackInfo selectByPrimaryKey(Integer callBackId);

    int updateByExampleSelective(@Param("record") CallBackInfo record, @Param("example") CallBackInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") CallBackInfo record, @Param("example") CallBackInfoExample example);

    int updateByExample(@Param("record") CallBackInfo record, @Param("example") CallBackInfoExample example);

    int updateByPrimaryKeySelective(CallBackInfo record);

    int updateByPrimaryKeyWithBLOBs(CallBackInfo record);

    int updateByPrimaryKey(CallBackInfo record);
}