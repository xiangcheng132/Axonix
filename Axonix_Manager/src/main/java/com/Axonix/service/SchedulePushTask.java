//package com.Axonix.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//import java.util.concurrent.ThreadLocalRandom;
//
////透传消息测试用，不在实际使用
//@Component
//@RequiredArgsConstructor
//public class SchedulePushTask {
//    private final HuaweiPushService pushService;
//
//    private static final String TEST_DEVICE_TOKEN = "IQAAAACy1KCuAABtLpl16xgxj0gUvCH7Hukha2o9dcBkeA2X31jK-Ega21lj2X6Kl1dGgEsHWJr3-Jyd1R9yyYptZlE8LORIBnDF63JqBI3-L9kBiA";
//
//    @Scheduled(fixedRate = 20_000)
//    public void sendPeriodicNotification() {
//        String randomTitle = generateRandomTitle();
//        String randomBody = generateRandomBody();
//
//        System.out.println("发送一次：title=" + randomTitle + ", body=" + randomBody);
//
//        pushService.sendPush(
//                TEST_DEVICE_TOKEN,
//                randomTitle,
//                randomBody
//        );
//    }
//
//    private String generateRandomTitle() {
//        return "推送标题-" + ThreadLocalRandom.current().nextInt(1000, 9999);
//    }
//
//    private String generateRandomBody() {
//        return "内容-" + UUID.randomUUID().toString().substring(0, 8); // 8位UUID随机片段
//    }
//}
