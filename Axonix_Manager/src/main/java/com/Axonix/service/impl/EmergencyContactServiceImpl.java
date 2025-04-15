package com.Axonix.service.impl;

import com.Axonix.demo.dto.EmergencyContactDto;
import com.Axonix.demo.mapper.EmergencyContactMapper;
import com.Axonix.demo.model.EmergencyContact;
import com.Axonix.demo.model.EmergencyContactExample;
import com.Axonix.service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    @Autowired
    private EmergencyContactMapper emergencyContactMapper;

    @Override
    public long countByExample(EmergencyContactExample example) {
        return emergencyContactMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(EmergencyContactExample example) {
        return emergencyContactMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return emergencyContactMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(EmergencyContact record) {
        return emergencyContactMapper.insert(record);
    }

    @Override
    public int insertSelective(EmergencyContact record) {
        return emergencyContactMapper.insertSelective(record);
    }

    @Override
    public List<EmergencyContact> selectByExample(EmergencyContactExample example) {
        return emergencyContactMapper.selectByExample(example);
    }

    @Override
    public EmergencyContact selectByPrimaryKey(Integer id) {
        return emergencyContactMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(EmergencyContact record, EmergencyContactExample example) {
        return emergencyContactMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(EmergencyContact record, EmergencyContactExample example) {
        return emergencyContactMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(EmergencyContact record) {
        return emergencyContactMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(EmergencyContact record) {
        return emergencyContactMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<EmergencyContactDto> getEmergencyContactDetail(Integer userId) {
        return emergencyContactMapper.selectByUserIdWithUsername(userId);
    }
}