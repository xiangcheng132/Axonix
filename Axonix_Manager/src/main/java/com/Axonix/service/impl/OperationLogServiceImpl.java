package com.Axonix.service.impl;

import com.Axonix.demo.mapper.OperationLogMapper;
import com.Axonix.demo.model.OperationLog;
import com.Axonix.demo.model.OperationLogExample;
import com.Axonix.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public long countByExample(OperationLogExample example) {
        return operationLogMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(OperationLogExample example) {
        return operationLogMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return operationLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OperationLog record) {
        return operationLogMapper.insert(record);
    }

    @Override
    public int insertSelective(OperationLog record) {
        return operationLogMapper.insertSelective(record);
    }

    @Override
    public List<OperationLog> selectByExample(OperationLogExample example) {
        return operationLogMapper.selectByExample(example);
    }

    @Override
    public OperationLog selectById(Integer id) {
        return operationLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(OperationLog record, OperationLogExample example) {
        return operationLogMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(OperationLog record, OperationLogExample example) {
        return operationLogMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(OperationLog record) {
        return operationLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateById(OperationLog record) {
        return operationLogMapper.updateByPrimaryKey(record);
    }
}
