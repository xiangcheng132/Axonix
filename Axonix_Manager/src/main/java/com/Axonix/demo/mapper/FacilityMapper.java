package com.Axonix.demo.mapper;

import com.Axonix.demo.model.Facility;
import com.Axonix.demo.model.FacilityExample;
import com.Axonix.demo.model.FacilityWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FacilityMapper {
    long countByExample(FacilityExample example);

    int deleteByExample(FacilityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FacilityWithBLOBs record);

    int insertSelective(FacilityWithBLOBs record);

    List<FacilityWithBLOBs> selectByExampleWithBLOBs(FacilityExample example);

    List<Facility> selectByExample(FacilityExample example);

    FacilityWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FacilityWithBLOBs record, @Param("example") FacilityExample example);

    int updateByExampleWithBLOBs(@Param("record") FacilityWithBLOBs record, @Param("example") FacilityExample example);

    int updateByExample(@Param("record") Facility record, @Param("example") FacilityExample example);

    int updateByPrimaryKeySelective(FacilityWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(FacilityWithBLOBs record);

    int updateByPrimaryKey(Facility record);
}