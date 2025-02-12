package com.Axonix.service;

import com.Axonix.demo.model.Facility;
import com.Axonix.demo.model.FacilityExample;
import com.Axonix.demo.model.FacilityWithBLOBs;
import java.util.List;

public interface FacilityService {

    long countByExample(FacilityExample example);

    int deleteByExample(FacilityExample example);

    int deleteById(Integer id);

    int insert(FacilityWithBLOBs record);

    int insertSelective(FacilityWithBLOBs record);

    List<FacilityWithBLOBs> selectByExampleWithBLOBs(FacilityExample example);

    List<Facility> selectByExample(FacilityExample example);

    FacilityWithBLOBs selectById(Integer id);

    int updateByExampleSelective(FacilityWithBLOBs record, FacilityExample example);

    int updateByExampleWithBLOBs(FacilityWithBLOBs record, FacilityExample example);

    int updateByExample(Facility record, FacilityExample example);

    int updateByIdSelective(FacilityWithBLOBs record);

    int updateByIdWithBLOBs(FacilityWithBLOBs record);

    int updateById(Facility record);
}
