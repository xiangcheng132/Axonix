package com.Axonix.service.impl;

import com.Axonix.demo.mapper.FunctionStatMapper;
import com.Axonix.demo.model.FunctionStat;
import com.Axonix.demo.model.FunctionStatExample;
import com.Axonix.service.FunctionStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FunctionStatServiceImpl implements FunctionStatService {

    @Autowired
    private FunctionStatMapper functionStatMapper;

    @Override
    public long countByExample(FunctionStatExample example) {
        return functionStatMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(FunctionStatExample example) {
        return functionStatMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return functionStatMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FunctionStat record) {
        return functionStatMapper.insert(record);
    }

    @Override
    public int insertSelective(FunctionStat record) {
        return functionStatMapper.insertSelective(record);
    }

    @Override
    public List<FunctionStat> selectByExample(FunctionStatExample example) {
        return functionStatMapper.selectByExample(example);
    }

    @Override
    public FunctionStat selectByPrimaryKey(Integer id) {
        return functionStatMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(FunctionStat record, FunctionStatExample example) {
        return functionStatMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(FunctionStat record, FunctionStatExample example) {
        return functionStatMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(FunctionStat record) {
        return functionStatMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(FunctionStat record) {
        return functionStatMapper.updateByPrimaryKey(record);
    }
}