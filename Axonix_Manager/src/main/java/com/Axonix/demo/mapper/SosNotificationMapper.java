package com.Axonix.demo.mapper;

import com.Axonix.demo.model.SosNotification;
import com.Axonix.demo.model.SosNotificationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SosNotificationMapper {
    long countByExample(SosNotificationExample example);

    int deleteByExample(SosNotificationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SosNotification record);

    int insertSelective(SosNotification record);

    List<SosNotification> selectByExampleWithBLOBs(SosNotificationExample example);

    List<SosNotification> selectByExample(SosNotificationExample example);

    SosNotification selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SosNotification record, @Param("example") SosNotificationExample example);

    int updateByExampleWithBLOBs(@Param("record") SosNotification record, @Param("example") SosNotificationExample example);

    int updateByExample(@Param("record") SosNotification record, @Param("example") SosNotificationExample example);

    int updateByPrimaryKeySelective(SosNotification record);

    int updateByPrimaryKeyWithBLOBs(SosNotification record);

    int updateByPrimaryKey(SosNotification record);
}