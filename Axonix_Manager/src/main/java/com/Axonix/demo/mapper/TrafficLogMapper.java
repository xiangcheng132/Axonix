package com.Axonix.demo.mapper;

import com.Axonix.demo.model.TrafficLog;
import com.Axonix.demo.model.TrafficLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TrafficLogMapper {
    long countByExample(TrafficLogExample example);

    int deleteByExample(TrafficLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TrafficLog record);

    int insertSelective(TrafficLog record);

    List<TrafficLog> selectByExample(TrafficLogExample example);

    TrafficLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TrafficLog record, @Param("example") TrafficLogExample example);

    int updateByExample(@Param("record") TrafficLog record, @Param("example") TrafficLogExample example);

    int updateByPrimaryKeySelective(TrafficLog record);

    int updateByPrimaryKey(TrafficLog record);
}