package com.Axonix.service;

import com.Axonix.demo.model.HelpRequest;
import com.Axonix.demo.model.HelpRequestExample;
import java.util.List;

public interface HelpRequestService {
    long countByExample(HelpRequestExample example);

    int deleteByExample(HelpRequestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HelpRequest record);

    int insertSelective(HelpRequest record);

    List<HelpRequest> selectByExample(HelpRequestExample example);

    HelpRequest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(HelpRequest record, HelpRequestExample example);

    int updateByExample(HelpRequest record, HelpRequestExample example);

    int updateByPrimaryKeySelective(HelpRequest record);

    int updateByPrimaryKey(HelpRequest record);

}