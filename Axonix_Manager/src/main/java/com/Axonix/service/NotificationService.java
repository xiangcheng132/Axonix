package com.Axonix.service;

import com.Axonix.demo.model.Notification;
import com.Axonix.demo.model.NotificationExample;
import java.util.List;

public interface NotificationService {
    long countByExample(NotificationExample example);

    int deleteByExample(NotificationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Notification record);

    int insertSelective(Notification record);

    List<Notification> selectByExampleWithBLOBs(NotificationExample example);

    List<Notification> selectByExample(NotificationExample example);

    Notification selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Notification record, NotificationExample example);

    int updateByExampleWithBLOBs(Notification record, NotificationExample example);

    int updateByExample(Notification record, NotificationExample example);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKeyWithBLOBs(Notification record);

    int updateByPrimaryKey(Notification record);
}