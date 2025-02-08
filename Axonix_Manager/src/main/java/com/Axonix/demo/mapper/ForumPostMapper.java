package com.Axonix.demo.mapper;

import com.Axonix.demo.model.ForumPost;
import com.Axonix.demo.model.ForumPostExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ForumPostMapper {
    long countByExample(ForumPostExample example);

    int deleteByExample(ForumPostExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ForumPost record);

    int insertSelective(ForumPost record);

    List<ForumPost> selectByExampleWithBLOBs(ForumPostExample example);

    List<ForumPost> selectByExample(ForumPostExample example);

    ForumPost selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ForumPost record, @Param("example") ForumPostExample example);

    int updateByExampleWithBLOBs(@Param("record") ForumPost record, @Param("example") ForumPostExample example);

    int updateByExample(@Param("record") ForumPost record, @Param("example") ForumPostExample example);

    int updateByPrimaryKeySelective(ForumPost record);

    int updateByPrimaryKeyWithBLOBs(ForumPost record);

    int updateByPrimaryKey(ForumPost record);
}