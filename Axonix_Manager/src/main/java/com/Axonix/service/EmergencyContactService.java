package com.Axonix.service;

import com.Axonix.demo.model.EmergencyContact;
import com.Axonix.demo.model.EmergencyContactExample;
import java.util.List;

public interface EmergencyContactService {
    long countByExample(EmergencyContactExample example);

    int deleteByExample(EmergencyContactExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EmergencyContact record);

    int insertSelective(EmergencyContact record);

    List<EmergencyContact> selectByExample(EmergencyContactExample example);

    EmergencyContact selectByPrimaryKey(Integer id);

    int updateByExampleSelective(EmergencyContact record, EmergencyContactExample example);

    int updateByExample(EmergencyContact record, EmergencyContactExample example);

    int updateByPrimaryKeySelective(EmergencyContact record);

    int updateByPrimaryKey(EmergencyContact record);
}