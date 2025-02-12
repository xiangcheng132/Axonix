package com.Axonix.service;

import com.Axonix.demo.model.OperationLog;
import com.Axonix.demo.model.OperationLogExample;
import java.util.List;

public interface OperationLogService {

    long countByExample(OperationLogExample example);

    int deleteByExample(OperationLogExample example);

    int deleteById(Integer id);

    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    List<OperationLog> selectByExample(OperationLogExample example);

    OperationLog selectById(Integer id);

    int updateByExampleSelective(OperationLog record, OperationLogExample example);

    int updateByExample(OperationLog record, OperationLogExample example);

    int updateByIdSelective(OperationLog record);

    int updateById(OperationLog record);
}
