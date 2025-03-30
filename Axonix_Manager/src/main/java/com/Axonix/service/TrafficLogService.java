package com.Axonix.service;

import com.Axonix.demo.model.TrafficLog;
import com.Axonix.demo.model.TrafficLogExample;
import java.util.List;

public interface TrafficLogService {
    long countByExample(TrafficLogExample example);

    int deleteByExample(TrafficLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TrafficLog record);

    int insertSelective(TrafficLog record);

    List<TrafficLog> selectByExample(TrafficLogExample example);

    TrafficLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(TrafficLog record, TrafficLogExample example);

    int updateByExample(TrafficLog record, TrafficLogExample example);

    int updateByPrimaryKeySelective(TrafficLog record);

    int updateByPrimaryKey(TrafficLog record);
}