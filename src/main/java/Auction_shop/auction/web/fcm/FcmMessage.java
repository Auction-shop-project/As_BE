package Auction_shop.auction.web.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

}
