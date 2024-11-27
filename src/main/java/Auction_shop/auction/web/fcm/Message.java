package Auction_shop.auction.web.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Message {
    private Notification notification;
    private String token;
    private Map<String, String> data;
}
