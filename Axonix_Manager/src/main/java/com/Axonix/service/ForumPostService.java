package com.Axonix.service;

import com.Axonix.demo.model.ForumPost;
import com.Axonix.demo.model.ForumPostExample;
import java.util.List;

public interface ForumPostService {

    long countByExample(ForumPostExample example);

    int deleteByExample(ForumPostExample example);

    int deleteById(Integer id);

    int insert(ForumPost record);

    int insertSelective(ForumPost record);

    List<ForumPost> selectByExampleWithBLOBs(ForumPostExample example);

    List<ForumPost> selectByExample(ForumPostExample example);

    ForumPost selectById(Integer id);

    int updateByExampleSelective(ForumPost record, ForumPostExample example);

    int updateByExampleWithBLOBs(ForumPost record, ForumPostExample example);

    int updateByExample(ForumPost record, ForumPostExample example);

    int updateByIdSelective(ForumPost record);

    int updateByIdWithBLOBs(ForumPost record);

    int updateById(ForumPost record);
}
