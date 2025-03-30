package com.Axonix.service;

import com.Axonix.demo.model.Feedback;
import com.Axonix.demo.model.FeedbackExample;
import java.util.List;

public interface FeedbackService {
    long countByExample(FeedbackExample example);

    int deleteByExample(FeedbackExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    List<Feedback> selectByExampleWithBLOBs(FeedbackExample example);

    List<Feedback> selectByExample(FeedbackExample example);

    Feedback selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Feedback record, FeedbackExample example);

    int updateByExampleWithBLOBs(Feedback record, FeedbackExample example);

    int updateByExample(Feedback record, FeedbackExample example);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKeyWithBLOBs(Feedback record);

    int updateByPrimaryKey(Feedback record);
}