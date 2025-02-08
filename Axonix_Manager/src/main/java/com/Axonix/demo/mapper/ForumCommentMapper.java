package com.Axonix.demo.mapper;

import com.Axonix.demo.model.ForumComment;
import com.Axonix.demo.model.ForumCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ForumCommentMapper {
    long countByExample(ForumCommentExample example);

    int deleteByExample(ForumCommentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ForumComment record);

    int insertSelective(ForumComment record);

    List<ForumComment> selectByExampleWithBLOBs(ForumCommentExample example);

    List<ForumComment> selectByExample(ForumCommentExample example);

    ForumComment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ForumComment record, @Param("example") ForumCommentExample example);

    int updateByExampleWithBLOBs(@Param("record") ForumComment record, @Param("example") ForumCommentExample example);

    int updateByExample(@Param("record") ForumComment record, @Param("example") ForumCommentExample example);

    int updateByPrimaryKeySelective(ForumComment record);

    int updateByPrimaryKeyWithBLOBs(ForumComment record);

    int updateByPrimaryKey(ForumComment record);
}