package com.Axonix.service.impl;

import com.Axonix.demo.mapper.ForumCategoryMapper;
import com.Axonix.demo.model.ForumCategory;
import com.Axonix.demo.model.ForumCategoryExample;
import com.Axonix.service.ForumCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForumCategoryServiceImpl implements ForumCategoryService {

    @Autowired
    private ForumCategoryMapper forumCategoryMapper;

    @Override
    public long countByExample(ForumCategoryExample example) {
        return forumCategoryMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ForumCategoryExample example) {
        return forumCategoryMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return forumCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ForumCategory record) {
        return forumCategoryMapper.insert(record);
    }

    @Override
    public int insertSelective(ForumCategory record) {
        return forumCategoryMapper.insertSelective(record);
    }

    @Override
    public List<ForumCategory> selectByExampleWithBLOBs(ForumCategoryExample example) {
        return forumCategoryMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ForumCategory> selectByExample(ForumCategoryExample example) {
        return forumCategoryMapper.selectByExample(example);
    }

    @Override
    public ForumCategory selectById(Integer id) {
        return forumCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(ForumCategory record, ForumCategoryExample example) {
        return forumCategoryMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(ForumCategory record, ForumCategoryExample example) {
        return forumCategoryMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(ForumCategory record, ForumCategoryExample example) {
        return forumCategoryMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(ForumCategory record) {
        return forumCategoryMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(ForumCategory record) {
        return forumCategoryMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(ForumCategory record) {
        return forumCategoryMapper.updateByPrimaryKey(record);
    }
}
