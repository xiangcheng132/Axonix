package com.Axonix.service.impl;

import com.Axonix.demo.mapper.AiLogMapper;
import com.Axonix.demo.model.AiLog;
import com.Axonix.demo.model.AiLogExample;
import com.Axonix.service.AiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AiLogServiceImpl implements AiLogService {

    @Autowired
    private AiLogMapper aiLogMapper;

    @Override
    public long countByExample(AiLogExample example) {
        return aiLogMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(AiLogExample example) {
        return aiLogMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return aiLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AiLog record) {
        return aiLogMapper.insert(record);
    }

    @Override
    public int insertSelective(AiLog record) {
        return aiLogMapper.insertSelective(record);
    }

    @Override
    public List<AiLog> selectByExample(AiLogExample example) {
        return aiLogMapper.selectByExample(example);
    }

    @Override
    public AiLog selectByPrimaryKey(Integer id) {
        return aiLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(AiLog record, AiLogExample example) {
        return aiLogMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(AiLog record, AiLogExample example) {
        return aiLogMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(AiLog record) {
        return aiLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AiLog record) {
        return aiLogMapper.updateByPrimaryKey(record);
    }
}