package Auction_shop.auction.web.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponseDto {
    private Long id;;
    private String memberNickname;
    private String content;
    private String alertType;
}
