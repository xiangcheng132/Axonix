package com.Axonix.service;

import com.Axonix.demo.model.LocationShare;
import com.Axonix.demo.model.LocationShareExample;
import java.util.List;

public interface LocationShareService {

    long countByExample(LocationShareExample example);

    int deleteByExample(LocationShareExample example);

    int deleteById(Integer id);

    int insert(LocationShare record);

    int insertSelective(LocationShare record);

    List<LocationShare> selectByExampleWithBLOBs(LocationShareExample example);

    List<LocationShare> selectByExample(LocationShareExample example);

    LocationShare selectById(Integer id);

    int updateByExampleSelective(LocationShare record, LocationShareExample example);

    int updateByExampleWithBLOBs(LocationShare record, LocationShareExample example);

    int updateByExample(LocationShare record, LocationShareExample example);

    int updateByIdSelective(LocationShare record);

    int updateByIdWithBLOBs(LocationShare record);

    int updateById(LocationShare record);
}
