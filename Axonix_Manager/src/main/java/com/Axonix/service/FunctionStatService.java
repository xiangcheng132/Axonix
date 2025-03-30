package com.Axonix.service;

import com.Axonix.demo.model.FunctionStat;
import com.Axonix.demo.model.FunctionStatExample;
import java.util.List;

public interface FunctionStatService {
    long countByExample(FunctionStatExample example);

    int deleteByExample(FunctionStatExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FunctionStat record);

    int insertSelective(FunctionStat record);

    List<FunctionStat> selectByExample(FunctionStatExample example);

    FunctionStat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(FunctionStat record, FunctionStatExample example);

    int updateByExample(FunctionStat record, FunctionStatExample example);

    int updateByPrimaryKeySelective(FunctionStat record);

    int updateByPrimaryKey(FunctionStat record);
}