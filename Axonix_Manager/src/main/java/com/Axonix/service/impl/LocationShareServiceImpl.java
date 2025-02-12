package com.Axonix.service.impl;

import com.Axonix.demo.mapper.LocationShareMapper;
import com.Axonix.demo.model.LocationShare;
import com.Axonix.demo.model.LocationShareExample;
import com.Axonix.service.LocationShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationShareServiceImpl implements LocationShareService {

    @Autowired
    private LocationShareMapper locationShareMapper;

    @Override
    public long countByExample(LocationShareExample example) {
        return locationShareMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(LocationShareExample example) {
        return locationShareMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return locationShareMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(LocationShare record) {
        return locationShareMapper.insert(record);
    }

    @Override
    public int insertSelective(LocationShare record) {
        return locationShareMapper.insertSelective(record);
    }

    @Override
    public List<LocationShare> selectByExampleWithBLOBs(LocationShareExample example) {
        return locationShareMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<LocationShare> selectByExample(LocationShareExample example) {
        return locationShareMapper.selectByExample(example);
    }

    @Override
    public LocationShare selectById(Integer id) {
        return locationShareMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(LocationShare record, LocationShareExample example) {
        return locationShareMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(LocationShare record, LocationShareExample example) {
        return locationShareMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(LocationShare record, LocationShareExample example) {
        return locationShareMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(LocationShare record) {
        return locationShareMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(LocationShare record) {
        return locationShareMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(LocationShare record) {
        return locationShareMapper.updateByPrimaryKey(record);
    }
}
