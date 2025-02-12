package com.Axonix.service.impl;

import com.Axonix.demo.mapper.MessageMapper;
import com.Axonix.demo.model.Message;
import com.Axonix.demo.model.MessageExample;
import com.Axonix.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public long countByExample(MessageExample example) {
        return messageMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(MessageExample example) {
        return messageMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Message record) {
        return messageMapper.insert(record);
    }

    @Override
    public int insertSelective(Message record) {
        return messageMapper.insertSelective(record);
    }

    @Override
    public List<Message> selectByExampleWithBLOBs(MessageExample example) {
        return messageMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Message> selectByExample(MessageExample example) {
        return messageMapper.selectByExample(example);
    }

    @Override
    public Message selectById(Integer id) {
        return messageMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Message record, MessageExample example) {
        return messageMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(Message record, MessageExample example) {
        return messageMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(Message record, MessageExample example) {
        return messageMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(Message record) {
        return messageMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(Message record) {
        return messageMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(Message record) {
        return messageMapper.updateByPrimaryKey(record);
    }
}
