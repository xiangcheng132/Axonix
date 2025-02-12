package com.Axonix.service;

import com.Axonix.demo.model.ForumCategory;
import com.Axonix.demo.model.ForumCategoryExample;
import java.util.List;

public interface ForumCategoryService {

    long countByExample(ForumCategoryExample example);

    int deleteByExample(ForumCategoryExample example);

    int deleteById(Integer id);

    int insert(ForumCategory record);

    int insertSelective(ForumCategory record);

    List<ForumCategory> selectByExampleWithBLOBs(ForumCategoryExample example);

    List<ForumCategory> selectByExample(ForumCategoryExample example);

    ForumCategory selectById(Integer id);

    int updateByExampleSelective(ForumCategory record, ForumCategoryExample example);

    int updateByExampleWithBLOBs(ForumCategory record, ForumCategoryExample example);

    int updateByExample(ForumCategory record, ForumCategoryExample example);

    int updateByIdSelective(ForumCategory record);

    int updateByIdWithBLOBs(ForumCategory record);

    int updateById(ForumCategory record);
}
