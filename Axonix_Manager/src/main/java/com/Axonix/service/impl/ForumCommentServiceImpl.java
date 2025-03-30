package com.Axonix.service.impl;

import com.Axonix.demo.mapper.ForumCommentMapper;
import com.Axonix.demo.model.ForumComment;
import com.Axonix.demo.model.ForumCommentExample;
import com.Axonix.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ForumCommentServiceImpl implements ForumCommentService {

    @Autowired
    private ForumCommentMapper forumCommentMapper;

    @Override
    public long countByExample(ForumCommentExample example) {
        return forumCommentMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ForumCommentExample example) {
        return forumCommentMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return forumCommentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ForumComment record) {
        return forumCommentMapper.insert(record);
    }

    @Override
    public int insertSelective(ForumComment record) {
        return forumCommentMapper.insertSelective(record);
    }

    @Override
    public List<ForumComment> selectByExampleWithBLOBs(ForumCommentExample example) {
        return forumCommentMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<ForumComment> selectByExample(ForumCommentExample example) {
        return forumCommentMapper.selectByExample(example);
    }

    @Override
    public ForumComment selectByPrimaryKey(Integer id) {
        return forumCommentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(ForumComment record, ForumCommentExample example) {
        return forumCommentMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(ForumComment record, ForumCommentExample example) {
        return forumCommentMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(ForumComment record, ForumCommentExample example) {
        return forumCommentMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(ForumComment record) {
        return forumCommentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(ForumComment record) {
        return forumCommentMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(ForumComment record) {
        return forumCommentMapper.updateByPrimaryKey(record);
    }
}