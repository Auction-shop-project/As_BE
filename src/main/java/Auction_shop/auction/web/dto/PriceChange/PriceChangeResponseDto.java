package Auction_shop.auction.web.dto.PriceChange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceChangeResponseDto {

    private int reducedPrice;
    private int newPrice;
    private LocalDateTime changeDate;
    private int changeOrder;

}
