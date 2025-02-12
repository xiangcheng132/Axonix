package com.Axonix.service.impl;

import com.Axonix.demo.mapper.FacilityMapper;
import com.Axonix.demo.model.Facility;
import com.Axonix.demo.model.FacilityExample;
import com.Axonix.demo.model.FacilityWithBLOBs;
import com.Axonix.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityMapper facilityMapper;

    @Override
    public long countByExample(FacilityExample example) {
        return facilityMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(FacilityExample example) {
        return facilityMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return facilityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FacilityWithBLOBs record) {
        return facilityMapper.insert(record);
    }

    @Override
    public int insertSelective(FacilityWithBLOBs record) {
        return facilityMapper.insertSelective(record);
    }

    @Override
    public List<FacilityWithBLOBs> selectByExampleWithBLOBs(FacilityExample example) {
        return facilityMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Facility> selectByExample(FacilityExample example) {
        return facilityMapper.selectByExample(example);
    }

    @Override
    public FacilityWithBLOBs selectById(Integer id) {
        return facilityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(FacilityWithBLOBs record, FacilityExample example) {
        return facilityMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(FacilityWithBLOBs record, FacilityExample example) {
        return facilityMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(Facility record, FacilityExample example) {
        return facilityMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(FacilityWithBLOBs record) {
        return facilityMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(FacilityWithBLOBs record) {
        return facilityMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(Facility record) {
        return facilityMapper.updateByPrimaryKey(record);
    }
}
