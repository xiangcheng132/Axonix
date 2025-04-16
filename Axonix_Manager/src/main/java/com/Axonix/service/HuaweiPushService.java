package com.Axonix.service;

import com.Axonix.config.HuaweiPushProperties;
import com.Axonix.demo.dto.PushMessageRequest;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import java.util.List;


//以下是透传消息服务，不是通知消息，透传消息与通知消息有一定区别，阅读者请自行查资料
@Service
@RequiredArgsConstructor
public class HuaweiPushService {
    private final HuaweiPushProperties properties;
    private final WebClient webClient = WebClient.create();

    public String getAccessToken() {
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> {
            try {
                sslContextSpec.sslContext(SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        String url = UriComponentsBuilder.fromHttpUrl("https://localhost:8080/test-token")
                .queryParam("grant_type", "client_credentials")
                .queryParam("client_id", properties.getClientId())
                .queryParam("client_secret", properties.getClientSecret())
                .toUriString();

        System.out.println("URL:"+url);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // 同步阻塞方式获取响应
    }

    public void sendPush(String deviceToken, String title, String body) {
        try {
            String accessToken = getAccessToken();
            System.out.println("accessToken：" + accessToken);
            String pushUrl = properties.getPushUrl().replace("{appId}", properties.getAppId());

            PushMessageRequest request = buildPushRequest(deviceToken, title, body);

            String response = webClient.post()
                    .uri(pushUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("推送响应：" + response);
        } catch (Exception e) {
            System.out.println("推送失败" + e.getMessage());
        }
    }

    private PushMessageRequest buildPushRequest(String deviceToken, String title, String body) {
        PushMessageRequest request = new PushMessageRequest();
        PushMessageRequest.Message message = new PushMessageRequest.Message();
        message.setToken(List.of(deviceToken));

        String data = "{\"title\":\"" + title + "\",\"body\":\"" + body + "\"}";
        message.setData(data);

        request.setMessage(message);
        return request;
    }
}
