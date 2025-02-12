package com.Axonix.service.impl;

import com.Axonix.demo.mapper.ForumPostMapper;
import com.Axonix.demo.model.ForumPost;
import com.Axonix.demo.model.ForumPostExample;
import com.Axonix.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForumPostServiceImpl implements ForumPostService {

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Override
    public long countByExample(ForumPostExample example) {
        return forumPostMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ForumPostExample example) {
        return forumPostMapper.deleteByExample(example);
    }

    @Override
    public int deleteById(Integer id) {
        return forumPostMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ForumPost record) {
        return forumPostMapper.insert(record);
    }

    @Override
    public int insertSelective(ForumPost record) {
        return forumPostMapper.insertSelective(record);
    }

    @Override
    public List<ForumPost> selectByExampleWithBLOBs(ForumPostExample example) {
        return forumPostMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ForumPost> selectByExample(ForumPostExample example) {
        return forumPostMapper.selectByExample(example);
    }

    @Override
    public ForumPost selectById(Integer id) {
        return forumPostMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(ForumPost record, ForumPostExample example) {
        return forumPostMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(ForumPost record, ForumPostExample example) {
        return forumPostMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(ForumPost record, ForumPostExample example) {
        return forumPostMapper.updateByExample(record, example);
    }

    @Override
    public int updateByIdSelective(ForumPost record) {
        return forumPostMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByIdWithBLOBs(ForumPost record) {
        return forumPostMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateById(ForumPost record) {
        return forumPostMapper.updateByPrimaryKey(record);
    }
}
