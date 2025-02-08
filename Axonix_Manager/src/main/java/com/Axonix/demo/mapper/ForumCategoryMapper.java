package com.Axonix.demo.mapper;

import com.Axonix.demo.model.ForumCategory;
import com.Axonix.demo.model.ForumCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ForumCategoryMapper {
    long countByExample(ForumCategoryExample example);

    int deleteByExample(ForumCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ForumCategory record);

    int insertSelective(ForumCategory record);

    List<ForumCategory> selectByExampleWithBLOBs(ForumCategoryExample example);

    List<ForumCategory> selectByExample(ForumCategoryExample example);

    ForumCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ForumCategory record, @Param("example") ForumCategoryExample example);

    int updateByExampleWithBLOBs(@Param("record") ForumCategory record, @Param("example") ForumCategoryExample example);

    int updateByExample(@Param("record") ForumCategory record, @Param("example") ForumCategoryExample example);

    int updateByPrimaryKeySelective(ForumCategory record);

    int updateByPrimaryKeyWithBLOBs(ForumCategory record);

    int updateByPrimaryKey(ForumCategory record);
}