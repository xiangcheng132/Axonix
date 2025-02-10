package com.Axonix.service;

import com.Axonix.demo.model.ThirdPartyApiLog;
import com.Axonix.demo.model.ThirdPartyApiLogExample;
import com.Axonix.demo.model.ThirdPartyApiLogWithBLOBs;

import java.util.List;

public interface ThirdPartyApiLogService {

    long countByExample(ThirdPartyApiLogExample example);

    int deleteByExample(ThirdPartyApiLogExample example);

    int deleteById(Integer id);

    int insert(ThirdPartyApiLogWithBLOBs record);

    int insertSelective(ThirdPartyApiLogWithBLOBs record);

    List<ThirdPartyApiLogWithBLOBs> selectByExampleWithBLOBs(ThirdPartyApiLogExample example);

    List<ThirdPartyApiLog> selectByExample(ThirdPartyApiLogExample example);

    ThirdPartyApiLogWithBLOBs selectById(Integer id);

    int updateByExampleSelective(ThirdPartyApiLogWithBLOBs record, ThirdPartyApiLogExample example);

    int updateByExampleWithBLOBs(ThirdPartyApiLogWithBLOBs record, ThirdPartyApiLogExample example);

    int updateByExample(ThirdPartyApiLog record, ThirdPartyApiLogExample example);

    int updateByIdSelective(ThirdPartyApiLogWithBLOBs record);

    int updateByIdWithBLOBs(ThirdPartyApiLogWithBLOBs record);

    int updateById(ThirdPartyApiLog record);
}
