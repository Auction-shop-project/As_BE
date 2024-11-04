package Auction_shop.auction.web.fcm.service;

import Auction_shop.auction.web.fcm.FcmMessage;
import Auction_shop.auction.web.fcm.Message;
import Auction_shop.auction.web.fcm.Notification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
            "auction-shop-a3d28/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body, String id, String type) throws IOException {
        String message = makeMessage(targetToken, title, body, id, type);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("title = " + title);
        System.out.println("body = " + body);
        System.out.println("푸시 알림 전송 완료");
        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body, String id, String type) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(Message.builder()
                        .token(targetToken)
                        .notification(Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .data(makeData(id, type))
                        .build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private Map<String, String> makeData(String id, String type){
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("type", type);
        return data;
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/serviceAccountKey.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
