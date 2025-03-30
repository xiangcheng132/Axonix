package com.Axonix.service.impl;

import com.Axonix.demo.mapper.HelpRequestMapper;
import com.Axonix.demo.model.HelpRequest;
import com.Axonix.demo.model.HelpRequestExample;
import com.Axonix.service.HelpRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HelpRequestServiceImpl implements HelpRequestService {

    @Autowired
    private HelpRequestMapper helpRequestMapper;

    @Override
    public long countByExample(HelpRequestExample example) {
        return helpRequestMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(HelpRequestExample example) {
        return helpRequestMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return helpRequestMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(HelpRequest record) {
        return helpRequestMapper.insert(record);
    }

    @Override
    public int insertSelective(HelpRequest record) {
        return helpRequestMapper.insertSelective(record);
    }

    @Override
    public List<HelpRequest> selectByExample(HelpRequestExample example) {
        return helpRequestMapper.selectByExample(example);
    }

    @Override
    public HelpRequest selectByPrimaryKey(Integer id) {
        return helpRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(HelpRequest record, HelpRequestExample example) {
        return helpRequestMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(HelpRequest record, HelpRequestExample example) {
        return helpRequestMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(HelpRequest record) {
        return helpRequestMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(HelpRequest record) {
        return helpRequestMapper.updateByPrimaryKey(record);
    }
}