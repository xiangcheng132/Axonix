package com.Axonix.service.impl;

import com.Axonix.demo.mapper.FriendRelationshipMapper;
import com.Axonix.demo.model.FriendRelationship;
import com.Axonix.demo.model.FriendRelationshipExample;
import com.Axonix.service.FriendRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FriendRelationshipServiceImpl implements FriendRelationshipService {

    @Autowired
    private FriendRelationshipMapper friendRelationshipMapper;

    @Override
    public long countByExample(FriendRelationshipExample example) {
        return friendRelationshipMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(FriendRelationshipExample example) {
        return friendRelationshipMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return friendRelationshipMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(FriendRelationship record) {
        return friendRelationshipMapper.insert(record);
    }

    @Override
    public int insertSelective(FriendRelationship record) {
        return friendRelationshipMapper.insertSelective(record);
    }

    @Override
    public List<FriendRelationship> selectByExample(FriendRelationshipExample example) {
        return friendRelationshipMapper.selectByExample(example);
    }

    @Override
    public FriendRelationship selectById(Integer id) {
        return friendRelationshipMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(FriendRelationship record, FriendRelationshipExample example) {
        return friendRelationshipMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(FriendRelationship record, FriendRelationshipExample example) {
        return friendRelationshipMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(FriendRelationship record) {
        return friendRelationshipMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateById(FriendRelationship record) {
        return friendRelationshipMapper.updateByPrimaryKey(record);
    }
}
