package com.Axonix.service.impl;

import com.Axonix.demo.mapper.UserMapper;
import com.Axonix.demo.mapper.VipMapper;
import com.Axonix.demo.model.Vip;
import com.Axonix.demo.model.VipExample;
import com.Axonix.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipServiceImpl implements VipService {

    // 依赖注入Mapper
    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private UserMapper userMapper;

    // ---------------------- 原有基础方法 ----------------------
    @Override
    public long countByExample(VipExample example) {
        return vipMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(VipExample example) {
        return vipMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return vipMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Vip record) {
        return vipMapper.insert(record);
    }

    @Override
    public int insertSelective(Vip record) {
        return vipMapper.insertSelective(record);
    }

    @Override
    public List<Vip> selectByExample(VipExample example) {
        return vipMapper.selectByExample(example);
    }

    @Override
    public Vip selectByPrimaryKey(Integer id) {
        return vipMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Vip record, VipExample example) {
        return vipMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(Vip record, VipExample example) {
        return vipMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(Vip record) {
        return vipMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Vip record) {
        return vipMapper.updateByPrimaryKey(record);
    }

    // ---------------------- 新增方法：查询过期用户ID ----------------------
    /**
     * 查询所有VIP已过期的用户ID
     * @param currentTime 当前时间
     * @return 过期用户的ID列表
     */
    public List<Integer> selectExpiredUserIds(Date currentTime) {
        return vipMapper.selectExpiredUserIds(currentTime);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 每天00:00执行
    @Transactional
    public void checkAndExpireVipTask() {
        Date currentTime = new Date();
        // 1. 查询所有过期VIP用户ID
        List<Integer> expiredUserIds = this.selectExpiredUserIds(currentTime);

        if (!expiredUserIds.isEmpty()) {
            // 2. 批量更新用户VIP状态
            int updatedCount = userMapper.batchUpdateVipStatus(expiredUserIds, 0);
            System.out.println("已更新 " + updatedCount + " 位用户的VIP状态");

            // 3. (可选) 清理过期VIP记录
            VipExample example = new VipExample();
            example.createCriteria().andEndTimeLessThanOrEqualTo(currentTime);
            vipMapper.deleteByExample(example);
        }
    }
}