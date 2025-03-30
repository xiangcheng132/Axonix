package com.Axonix.service.impl;

import com.Axonix.demo.mapper.TrafficLogMapper;
import com.Axonix.demo.model.TrafficLog;
import com.Axonix.demo.model.TrafficLogExample;
import com.Axonix.service.TrafficLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrafficLogServiceImpl implements TrafficLogService {

    @Autowired
    private TrafficLogMapper trafficLogMapper;

    @Override
    public long countByExample(TrafficLogExample example) {
        return trafficLogMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(TrafficLogExample example) {
        return trafficLogMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return trafficLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TrafficLog record) {
        return trafficLogMapper.insert(record);
    }

    @Override
    public int insertSelective(TrafficLog record) {
        return trafficLogMapper.insertSelective(record);
    }

    @Override
    public List<TrafficLog> selectByExample(TrafficLogExample example) {
        return trafficLogMapper.selectByExample(example);
    }

    @Override
    public TrafficLog selectByPrimaryKey(Integer id) {
        return trafficLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(TrafficLog record, TrafficLogExample example) {
        return trafficLogMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(TrafficLog record, TrafficLogExample example) {
        return trafficLogMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(TrafficLog record) {
        return trafficLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TrafficLog record) {
        return trafficLogMapper.updateByPrimaryKey(record);
    }
}