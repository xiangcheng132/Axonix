package com.Axonix.service;

import com.Axonix.demo.model.Vip;
import com.Axonix.demo.model.VipExample;
import java.util.List;

public interface VipService {
    long countByExample(VipExample example);

    int deleteByExample(VipExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Vip record);

    int insertSelective(Vip record);

    List<Vip> selectByExample(VipExample example);

    Vip selectByPrimaryKey(Integer id);

    int updateByExampleSelective(Vip record, VipExample example);

    int updateByExample(Vip record, VipExample example);

    int updateByPrimaryKeySelective(Vip record);

    int updateByPrimaryKey(Vip record);
}