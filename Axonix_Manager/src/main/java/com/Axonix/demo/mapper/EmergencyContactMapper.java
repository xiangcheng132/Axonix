package com.Axonix.demo.mapper;

import com.Axonix.demo.dto.EmergencyContactDto;
import com.Axonix.demo.model.EmergencyContact;
import com.Axonix.demo.model.EmergencyContactExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface EmergencyContactMapper {
    long countByExample(EmergencyContactExample example);

    int deleteByExample(EmergencyContactExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(EmergencyContact record);

    int insertSelective(EmergencyContact record);

    List<EmergencyContact> selectByExample(EmergencyContactExample example);

    EmergencyContact selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") EmergencyContact record, @Param("example") EmergencyContactExample example);

    int updateByExample(@Param("record") EmergencyContact record, @Param("example") EmergencyContactExample example);

    int updateByPrimaryKeySelective(EmergencyContact record);

    int updateByPrimaryKey(EmergencyContact record);

    List<EmergencyContactDto> selectByUserIdWithUsername(@Param("userId") Integer userId);
}