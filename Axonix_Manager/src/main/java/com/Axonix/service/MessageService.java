package com.Axonix.service;

import com.Axonix.demo.model.Message;
import com.Axonix.demo.model.MessageExample;
import java.util.List;

public interface MessageService {

    long countByExample(MessageExample example);

    int deleteByExample(MessageExample example);

    int deleteById(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    List<Message> selectByExampleWithBLOBs(MessageExample example);

    List<Message> selectByExample(MessageExample example);

    Message selectById(Integer id);

    int updateByExampleSelective(Message record, MessageExample example);

    int updateByExampleWithBLOBs(Message record, MessageExample example);

    int updateByExample(Message record, MessageExample example);

    int updateByIdSelective(Message record);

    int updateByIdWithBLOBs(Message record);

    int updateById(Message record);
}
