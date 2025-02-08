package com.Axonix.demo.mapper;

import com.Axonix.demo.model.EmergencyRequest;
import com.Axonix.demo.model.EmergencyRequestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmergencyRequestMapper {
    long countByExample(EmergencyRequestExample example);

    int deleteByExample(EmergencyRequestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EmergencyRequest record);

    int insertSelective(EmergencyRequest record);

    List<EmergencyRequest> selectByExampleWithBLOBs(EmergencyRequestExample example);

    List<EmergencyRequest> selectByExample(EmergencyRequestExample example);

    EmergencyRequest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EmergencyRequest record, @Param("example") EmergencyRequestExample example);

    int updateByExampleWithBLOBs(@Param("record") EmergencyRequest record, @Param("example") EmergencyRequestExample example);

    int updateByExample(@Param("record") EmergencyRequest record, @Param("example") EmergencyRequestExample example);

    int updateByPrimaryKeySelective(EmergencyRequest record);

    int updateByPrimaryKeyWithBLOBs(EmergencyRequest record);

    int updateByPrimaryKey(EmergencyRequest record);
}