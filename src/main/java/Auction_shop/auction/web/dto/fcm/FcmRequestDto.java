package Auction_shop.auction.web.dto.fcm;

import Auction_shop.auction.web.fcm.NotificationType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmRequestDto {
    private String targetToken;
    private String title;
    private String body;
    private NotificationType type;
}
