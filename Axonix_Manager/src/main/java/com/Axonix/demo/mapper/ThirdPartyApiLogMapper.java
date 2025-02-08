package com.Axonix.demo.mapper;

import com.Axonix.demo.model.ThirdPartyApiLog;
import com.Axonix.demo.model.ThirdPartyApiLogExample;
import com.Axonix.demo.model.ThirdPartyApiLogWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ThirdPartyApiLogMapper {
    long countByExample(ThirdPartyApiLogExample example);

    int deleteByExample(ThirdPartyApiLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ThirdPartyApiLogWithBLOBs record);

    int insertSelective(ThirdPartyApiLogWithBLOBs record);

    List<ThirdPartyApiLogWithBLOBs> selectByExampleWithBLOBs(ThirdPartyApiLogExample example);

    List<ThirdPartyApiLog> selectByExample(ThirdPartyApiLogExample example);

    ThirdPartyApiLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ThirdPartyApiLogWithBLOBs record, @Param("example") ThirdPartyApiLogExample example);

    int updateByExampleWithBLOBs(@Param("record") ThirdPartyApiLogWithBLOBs record, @Param("example") ThirdPartyApiLogExample example);

    int updateByExample(@Param("record") ThirdPartyApiLog record, @Param("example") ThirdPartyApiLogExample example);

    int updateByPrimaryKeySelective(ThirdPartyApiLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ThirdPartyApiLogWithBLOBs record);

    int updateByPrimaryKey(ThirdPartyApiLog record);
}