package com.Axonix.demo.mapper;

import com.Axonix.demo.model.HelpRequest;
import com.Axonix.demo.model.HelpRequestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HelpRequestMapper {
    long countByExample(HelpRequestExample example);

    int deleteByExample(HelpRequestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HelpRequest record);

    int insertSelective(HelpRequest record);

    List<HelpRequest> selectByExample(HelpRequestExample example);

    HelpRequest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HelpRequest record, @Param("example") HelpRequestExample example);

    int updateByExample(@Param("record") HelpRequest record, @Param("example") HelpRequestExample example);

    int updateByPrimaryKeySelective(HelpRequest record);

    int updateByPrimaryKey(HelpRequest record);
}