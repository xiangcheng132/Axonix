package com.Axonix.service;

import com.Axonix.demo.model.ForumComment;
import com.Axonix.demo.model.ForumCommentExample;
import java.util.List;

public interface ForumCommentService {
    long countByExample(ForumCommentExample example);

    int deleteByExample(ForumCommentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ForumComment record);

    int insertSelective(ForumComment record);

    List<ForumComment> selectByExampleWithBLOBs(ForumCommentExample example);

    List<ForumComment> selectByExample(ForumCommentExample example);

    ForumComment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(ForumComment record, ForumCommentExample example);

    int updateByExampleWithBLOBs(ForumComment record, ForumCommentExample example);

    int updateByExample(ForumComment record, ForumCommentExample example);

    int updateByPrimaryKeySelective(ForumComment record);

    int updateByPrimaryKeyWithBLOBs(ForumComment record);

    int updateByPrimaryKey(ForumComment record);
}