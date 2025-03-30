package com.Axonix.service;

import com.Axonix.demo.model.SosNotification;
import com.Axonix.demo.model.SosNotificationExample;
import java.util.List;

public interface SosNotificationService {
    long countByExample(SosNotificationExample example);

    int deleteByExample(SosNotificationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SosNotification record);

    int insertSelective(SosNotification record);

    List<SosNotification> selectByExampleWithBLOBs(SosNotificationExample example);

    List<SosNotification> selectByExample(SosNotificationExample example);

    SosNotification selectByPrimaryKey(Integer id);

    int updateByExampleSelective(SosNotification record, SosNotificationExample example);

    int updateByExampleWithBLOBs(SosNotification record, SosNotificationExample example);

    int updateByExample(SosNotification record, SosNotificationExample example);

    int updateByPrimaryKeySelective(SosNotification record);

    int updateByPrimaryKeyWithBLOBs(SosNotification record);

    int updateByPrimaryKey(SosNotification record);
}