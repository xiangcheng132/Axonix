package com.Axonix.service;

import com.Axonix.demo.model.EmergencyRequest;
import com.Axonix.demo.model.EmergencyRequestExample;
import java.util.List;

public interface EmergencyRequestService {

    long countByExample(EmergencyRequestExample example);

    int deleteByExample(EmergencyRequestExample example);

    int deleteById(Integer id);

    int insert(EmergencyRequest record);

    int insertSelective(EmergencyRequest record);

    List<EmergencyRequest> selectByExampleWithBLOBs(EmergencyRequestExample example);

    List<EmergencyRequest> selectByExample(EmergencyRequestExample example);

    EmergencyRequest selectById(Integer id);

    int updateByExampleSelective(EmergencyRequest record, EmergencyRequestExample example);

    int updateByExampleWithBLOBs(EmergencyRequest record, EmergencyRequestExample example);

    int updateByExample(EmergencyRequest record, EmergencyRequestExample example);

    int updateByIdSelective(EmergencyRequest record);

    int updateByIdWithBLOBs(EmergencyRequest record);

    int updateById(EmergencyRequest record);
}
