package com.Axonix.demo.mapper;

import com.Axonix.demo.model.LocationShare;
import com.Axonix.demo.model.LocationShareExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LocationShareMapper {
    long countByExample(LocationShareExample example);

    int deleteByExample(LocationShareExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LocationShare record);

    int insertSelective(LocationShare record);

    List<LocationShare> selectByExampleWithBLOBs(LocationShareExample example);

    List<LocationShare> selectByExample(LocationShareExample example);

    LocationShare selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LocationShare record, @Param("example") LocationShareExample example);

    int updateByExampleWithBLOBs(@Param("record") LocationShare record, @Param("example") LocationShareExample example);

    int updateByExample(@Param("record") LocationShare record, @Param("example") LocationShareExample example);

    int updateByPrimaryKeySelective(LocationShare record);

    int updateByPrimaryKeyWithBLOBs(LocationShare record);

    int updateByPrimaryKey(LocationShare record);
}