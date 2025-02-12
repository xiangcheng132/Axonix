package com.Axonix.service.impl;

import com.Axonix.demo.mapper.NotificationMapper;
import com.Axonix.demo.model.Notification;
import com.Axonix.demo.model.NotificationExample;
import com.Axonix.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public long countByExample(NotificationExample example) {
        return notificationMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(NotificationExample example) {
        return notificationMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return notificationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Notification record) {
        return notificationMapper.insert(record);
    }

    @Override
    public int insertSelective(Notification record) {
        return notificationMapper.insertSelective(record);
    }

    @Override
    public List<Notification> selectByExampleWithBLOBs(NotificationExample example) {
        return notificationMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Notification> selectByExample(NotificationExample example) {
        return notificationMapper.selectByExample(example);
    }

    @Override
    public Notification selectById(Integer id) {
        return notificationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Notification record, NotificationExample example) {
        return notificationMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(Notification record, NotificationExample example) {
        return notificationMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(Notification record, NotificationExample example) {
        return notificationMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(Notification record) {
        return notificationMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(Notification record) {
        return notificationMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(Notification record) {
        return notificationMapper.updateByPrimaryKey(record);
    }
}
