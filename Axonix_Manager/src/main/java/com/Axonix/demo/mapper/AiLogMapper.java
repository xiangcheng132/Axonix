package com.Axonix.demo.mapper;

import com.Axonix.demo.model.AiLog;
import com.Axonix.demo.model.AiLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AiLogMapper {
    long countByExample(AiLogExample example);

    int deleteByExample(AiLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AiLog record);

    int insertSelective(AiLog record);

    List<AiLog> selectByExample(AiLogExample example);

    AiLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AiLog record, @Param("example") AiLogExample example);

    int updateByExample(@Param("record") AiLog record, @Param("example") AiLogExample example);

    int updateByPrimaryKeySelective(AiLog record);

    int updateByPrimaryKey(AiLog record);
}