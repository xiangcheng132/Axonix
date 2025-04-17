package com.Axonix.demo.mapper;

import com.Axonix.demo.model.FunctionStat;
import com.Axonix.demo.model.FunctionStatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FunctionStatMapper {
    long countByExample(FunctionStatExample example);

    int deleteByExample(FunctionStatExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FunctionStat record);

    int insertSelective(FunctionStat record);

    List<FunctionStat> selectByExample(FunctionStatExample example);

    FunctionStat selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FunctionStat record, @Param("example") FunctionStatExample example);

    int updateByExample(@Param("record") FunctionStat record, @Param("example") FunctionStatExample example);

    int updateByPrimaryKeySelective(FunctionStat record);

    int updateByPrimaryKey(FunctionStat record);

    int incrementField(@Param("userId") Integer userId, @Param("fieldName") String fieldName);

}