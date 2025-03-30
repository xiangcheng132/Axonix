package com.Axonix.service.impl;

import com.Axonix.demo.mapper.SosNotificationMapper;
import com.Axonix.demo.model.SosNotification;
import com.Axonix.demo.model.SosNotificationExample;
import com.Axonix.service.SosNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SosNotificationServiceImpl implements SosNotificationService {

    @Autowired
    private SosNotificationMapper sosNotificationMapper;

    @Override
    public long countByExample(SosNotificationExample example) {
        return sosNotificationMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(SosNotificationExample example) {
        return sosNotificationMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sosNotificationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SosNotification record) {
        return sosNotificationMapper.insert(record);
    }

    @Override
    public int insertSelective(SosNotification record) {
        return sosNotificationMapper.insertSelective(record);
    }

    @Override
    public List<SosNotification> selectByExampleWithBLOBs(SosNotificationExample example) {
        return sosNotificationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<SosNotification> selectByExample(SosNotificationExample example) {
        return sosNotificationMapper.selectByExample(example);
    }

    @Override
    public SosNotification selectByPrimaryKey(Integer id) {
        return sosNotificationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(SosNotification record, SosNotificationExample example) {
        return sosNotificationMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(SosNotification record, SosNotificationExample example) {
        return sosNotificationMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(SosNotification record, SosNotificationExample example) {
        return sosNotificationMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(SosNotification record) {
        return sosNotificationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(SosNotification record) {
        return sosNotificationMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(SosNotification record) {
        return sosNotificationMapper.updateByPrimaryKey(record);
    }
}