package com.Axonix.service.impl;

import com.Axonix.demo.mapper.FeedbackMapper;
import com.Axonix.demo.model.Feedback;
import com.Axonix.demo.model.FeedbackExample;
import com.Axonix.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public long countByExample(FeedbackExample example) {
        return feedbackMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(FeedbackExample example) {
        return feedbackMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return feedbackMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Feedback record) {
        return feedbackMapper.insert(record);
    }

    @Override
    public int insertSelective(Feedback record) {
        return feedbackMapper.insertSelective(record);
    }

    @Override
    public List<Feedback> selectByExampleWithBLOBs(FeedbackExample example) {
        return feedbackMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<Feedback> selectByExample(FeedbackExample example) {
        return feedbackMapper.selectByExample(example);
    }

    @Override
    public Feedback selectByPrimaryKey(Integer id) {
        return feedbackMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Feedback record, FeedbackExample example) {
        return feedbackMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExampleWithBLOBs(Feedback record, FeedbackExample example) {
        return feedbackMapper.updateByExampleWithBLOBs(record, example);
    }

    @Override
    public int updateByExample(Feedback record, FeedbackExample example) {
        return feedbackMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(Feedback record) {
        return feedbackMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Feedback record) {
        return feedbackMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(Feedback record) {
        return feedbackMapper.updateByPrimaryKey(record);
    }
}