package com.Axonix.service;

import com.Axonix.demo.model.FriendRelationship;
import com.Axonix.demo.model.FriendRelationshipExample;
import java.util.List;

public interface FriendRelationshipService {

    long countByExample(FriendRelationshipExample example);

    int deleteByExample(FriendRelationshipExample example);

    int deleteById(Integer id);

    int insert(FriendRelationship record);

    int insertSelective(FriendRelationship record);

    List<FriendRelationship> selectByExample(FriendRelationshipExample example);

    FriendRelationship selectById(Integer id);

    int updateByExampleSelective(FriendRelationship record, FriendRelationshipExample example);

    int updateByExample(FriendRelationship record, FriendRelationshipExample example);

    int updateByIdSelective(FriendRelationship record);

    int updateById(FriendRelationship record);
}
