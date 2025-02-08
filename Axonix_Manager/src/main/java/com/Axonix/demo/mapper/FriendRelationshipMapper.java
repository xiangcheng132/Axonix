package com.Axonix.demo.mapper;

import com.Axonix.demo.model.FriendRelationship;
import com.Axonix.demo.model.FriendRelationshipExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FriendRelationshipMapper {
    long countByExample(FriendRelationshipExample example);

    int deleteByExample(FriendRelationshipExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FriendRelationship record);

    int insertSelective(FriendRelationship record);

    List<FriendRelationship> selectByExample(FriendRelationshipExample example);

    FriendRelationship selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FriendRelationship record, @Param("example") FriendRelationshipExample example);

    int updateByExample(@Param("record") FriendRelationship record, @Param("example") FriendRelationshipExample example);

    int updateByPrimaryKeySelective(FriendRelationship record);

    int updateByPrimaryKey(FriendRelationship record);
}