package com.Axonix.service.impl;

import com.Axonix.demo.mapper.ThirdPartyApiLogMapper;
import com.Axonix.demo.model.ThirdPartyApiLog;
import com.Axonix.demo.model.ThirdPartyApiLogExample;
import com.Axonix.demo.model.ThirdPartyApiLogWithBLOBs;
import com.Axonix.service.ThirdPartyApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdPartyApiLogServiceImpl implements ThirdPartyApiLogService {

    @Autowired
    private ThirdPartyApiLogMapper thirdPartyApiLogMapper;

    @Override
    public long countByExample(ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return thirdPartyApiLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ThirdPartyApiLogWithBLOBs record) {
        return thirdPartyApiLogMapper.insert(record);
    }

    @Override
    public int insertSelective(ThirdPartyApiLogWithBLOBs record) {
        return thirdPartyApiLogMapper.insertSelective(record);
    }

    @Override
    public List<ThirdPartyApiLogWithBLOBs> selectByExampleWithBLOBs(ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ThirdPartyApiLog> selectByExample(ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.selectByExample(example);
    }

    @Override
    public ThirdPartyApiLogWithBLOBs selectById(Integer id) {
        return thirdPartyApiLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(ThirdPartyApiLogWithBLOBs record, ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(ThirdPartyApiLogWithBLOBs record, ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(ThirdPartyApiLog record, ThirdPartyApiLogExample example) {
        return thirdPartyApiLogMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(ThirdPartyApiLogWithBLOBs record) {
        return thirdPartyApiLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(ThirdPartyApiLogWithBLOBs record) {
        return thirdPartyApiLogMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(ThirdPartyApiLog record) {
        return thirdPartyApiLogMapper.updateByPrimaryKey(record);
    }
}
