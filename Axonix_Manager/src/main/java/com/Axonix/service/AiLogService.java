package com.Axonix.service;

import com.Axonix.demo.model.AiLog;
import com.Axonix.demo.model.AiLogExample;
import java.util.List;

public interface AiLogService {
    long countByExample(AiLogExample example);

    int deleteByExample(AiLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AiLog record);

    int insertSelective(AiLog record);

    List<AiLog> selectByExample(AiLogExample example);

    AiLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(AiLog record, AiLogExample example);

    int updateByExample(AiLog record, AiLogExample example);

    int updateByPrimaryKeySelective(AiLog record);

    int updateByPrimaryKey(AiLog record);
}