package com.Axonix.service;

import com.Axonix.demo.dto.HuaweiNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class HuaweiNotification {
    private final HuaweiPushService pushService;

    public void sendPeriodicNotification(HuaweiNotificationDto data) {
        pushService.sendPush(
                data.getToken(),
                data.getTitle(),
                data.getContent()
        );
    }

}
