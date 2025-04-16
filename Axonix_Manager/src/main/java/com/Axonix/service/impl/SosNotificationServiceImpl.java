package com.Axonix.service.impl;

import com.Axonix.demo.dto.HuaweiNotificationDto;
import com.Axonix.demo.mapper.SosNotificationMapper;
import com.Axonix.demo.model.SosNotification;
import com.Axonix.demo.model.SosNotificationExample;
import com.Axonix.demo.model.User;
import com.Axonix.service.HuaweiNotification;
import com.Axonix.service.SosNotificationService;
import com.Axonix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SosNotificationServiceImpl implements SosNotificationService {

    private final SosNotificationMapper sosNotificationMapper;
    private final UserService userService;
    private final HuaweiNotification huaweiNotification;

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
        record.setSendTime(new Date());
        //插入紧急sos表的同时需要进行通知
        User contact = userService.selectByPrimaryKey(record.getContactId());
        User user = userService.selectByPrimaryKey(record.getUserId());

        HuaweiNotificationDto dto = new HuaweiNotificationDto();
        dto.setToken(contact.getDeviceToken());
        dto.setTitle(record.getTitle());
        dto.setContent(user.getUsername() + " 在" +record.getContent()+ "\n经度："+record.getLng() + "   纬度：" + record.getLat());

        huaweiNotification.sendPeriodicNotification(dto);
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