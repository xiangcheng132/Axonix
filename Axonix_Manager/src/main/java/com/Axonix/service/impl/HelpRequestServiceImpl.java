package com.Axonix.service.impl;

import com.Axonix.demo.dto.HuaweiNotificationDto;
import com.Axonix.demo.mapper.HelpRequestMapper;
import com.Axonix.demo.model.HelpRequest;
import com.Axonix.demo.model.HelpRequestExample;
import com.Axonix.demo.model.User;
import com.Axonix.service.HelpRequestService;
import com.Axonix.service.HuaweiNotification;
import com.Axonix.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpRequestServiceImpl implements HelpRequestService {

    private final HelpRequestMapper helpRequestMapper;
    private final UserService userService;
    private final HuaweiNotification huaweiNotification;

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double DISTANCE_THRESHOLD_KM = 0.1;

    @Override
    public long countByExample(HelpRequestExample example) {
        return helpRequestMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(HelpRequestExample example) {
        return helpRequestMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return helpRequestMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(HelpRequest record) {
        return helpRequestMapper.insert(record);
    }

    @Override
    public int insertSelective(HelpRequest record) {
        return helpRequestMapper.insertSelective(record);
    }

    @Override
    public List<HelpRequest> selectByExample(HelpRequestExample example) {
        return helpRequestMapper.selectByExample(example);
    }

    @Override
    public HelpRequest selectByPrimaryKey(Integer id) {
        return helpRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(HelpRequest record, HelpRequestExample example) {
        return helpRequestMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(HelpRequest record, HelpRequestExample example) {
        return helpRequestMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(HelpRequest record) {
        return helpRequestMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(HelpRequest record) {
        return helpRequestMapper.updateByPrimaryKey(record);
    }



    // 定时任务：每10秒执行一次匹配
    @Scheduled(fixedRate = 10000)
    public void autoMatch() {
        HelpRequestExample example = new HelpRequestExample();
        example.createCriteria().andStatusEqualTo(1);
        List<HelpRequest> pendingRequests = helpRequestMapper.selectByExample(example);

        for (HelpRequest request : pendingRequests) {
            HelpRequest match = match(request);
            if (match != null) {
                matchPair(request, match);
            }
        }
    }

    // 核心匹配逻辑
    public HelpRequest match(HelpRequest incomingRequest) {
        if (incomingRequest.getStatus() != 1) return null;

        boolean isRequester = incomingRequest.getHelperId() == null;
        boolean isHelper = incomingRequest.getRequesterId() == null;

        if (!isRequester && !isHelper) return null;

        HelpRequestExample example = new HelpRequestExample();
        HelpRequestExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1)
                .andIdNotEqualTo(incomingRequest.getId());

        if (isRequester) {
            criteria.andRequesterIdIsNull(); // 匹配帮扶者
        } else if (isHelper) {
            criteria.andHelperIdIsNull(); // 匹配求助者
        }

        List<HelpRequest> candidates = helpRequestMapper.selectByExample(example);
        if (candidates.isEmpty()) return null;

        // 排序：优先最近，其次时间早
        candidates.sort(Comparator
                .comparingDouble((HelpRequest c) -> getDistance(incomingRequest, c))
                .thenComparing(HelpRequest::getCreatedAt));

        return candidates.get(0);
    }

    private void matchPair(HelpRequest r1, HelpRequest r2) {

        if (r1.getHelperId() == null) {
            r1.setHelperId(r2.getHelperId());
            r1.setStatus(2);
            r1.setHelperLat(r2.getHelperLat());
            r1.setHelperLng(r2.getHelperLng());
        } else {
            r1.setRequesterId(r2.getRequesterId());
            r1.setStatus(2);
            r1.setRequesterLat(r2.getRequesterLat());
            r1.setRequesterLng(r2.getRequesterLng());
        }
        r1.setUpdatedAt(new Date());
        helpRequestMapper.updateByPrimaryKeySelective(r1);
        helpRequestMapper.deleteByPrimaryKey(r2.getId());

        User user1 = userService.selectByPrimaryKey(r1.getHelperId());
        User user2 = userService.selectByPrimaryKey(r1.getRequesterId());

        HuaweiNotificationDto dto = new HuaweiNotificationDto();
        dto.setToken(user1.getDeviceToken());
        dto.setTitle("匹配成功");
        dto.setContent("您的匹配已成功，对方的位置在："+ "\n经度："+r1.getRequesterLng() + "   纬度：" + r1.getRequesterLat() + "\n点击通知跳转导航");
        huaweiNotification.sendPeriodicNotification(dto);

        dto.setToken(user2.getDeviceToken());
        dto.setTitle("匹配成功");
        dto.setContent("您的匹配已成功，对方的位置在："+ "\n经度："+r1.getHelperLng() + "   纬度：" + r1.getHelperLat() + "\n点击通知跳转导航");
        huaweiNotification.sendPeriodicNotification(dto);
    }


    private double getDistance(HelpRequest a, HelpRequest b) {
        BigDecimal lat1, lng1, lat2, lng2;

        if (a.getHelperId() == null) {
            lat1 = a.getRequesterLat();
            lng1 = a.getRequesterLng();
        } else {
            lat1 = a.getHelperLat();
            lng1 = a.getHelperLng();
        }

        if (b.getHelperId() == null) {
            lat2 = b.getRequesterLat();
            lng2 = b.getRequesterLng();
        } else {
            lat2 = b.getHelperLat();
            lng2 = b.getHelperLng();
        }

        return haversine(lat1.doubleValue(), lng1.doubleValue(),
                lat2.doubleValue(), lng2.doubleValue());
    }

    // Haversine 地球距离计算
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }


}