package com.Axonix.service.impl;

import com.Axonix.demo.mapper.EmergencyRequestMapper;
import com.Axonix.demo.model.EmergencyRequest;
import com.Axonix.demo.model.EmergencyRequestExample;
import com.Axonix.service.EmergencyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmergencyRequestServiceImpl implements EmergencyRequestService {

    @Autowired
    private EmergencyRequestMapper emergencyRequestMapper;

    @Override
    public long countByExample(EmergencyRequestExample example) {
        return emergencyRequestMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(EmergencyRequestExample example) {
        return emergencyRequestMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return emergencyRequestMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(EmergencyRequest record) {
        return emergencyRequestMapper.insert(record);
    }

    @Override
    public int insertSelective(EmergencyRequest record) {
        return emergencyRequestMapper.insertSelective(record);
    }

    @Override
    public List<EmergencyRequest> selectByExampleWithBLOBs(EmergencyRequestExample example) {
        return emergencyRequestMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<EmergencyRequest> selectByExample(EmergencyRequestExample example) {
        return emergencyRequestMapper.selectByExample(example);
    }

    @Override
    public EmergencyRequest selectById(Integer id) {
        return emergencyRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(EmergencyRequest record, EmergencyRequestExample example) {
        return emergencyRequestMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(EmergencyRequest record, EmergencyRequestExample example) {
        return emergencyRequestMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(EmergencyRequest record, EmergencyRequestExample example) {
        return emergencyRequestMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(EmergencyRequest record) {
        return emergencyRequestMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(EmergencyRequest record) {
        return emergencyRequestMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(EmergencyRequest record) {
        return emergencyRequestMapper.updateByPrimaryKey(record);
    }
}
