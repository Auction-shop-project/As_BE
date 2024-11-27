package Auction_shop.auction.web.dto.PriceChange;

import Auction_shop.auction.domain.priceChange.PriceChange;
import org.springframework.stereotype.Component;

@Component
public class PriceChangeMapperImpl implements PriceChangeMapper{

    @Override
    public PriceChangeResponseDto toResponseDto(PriceChange priceChange) {
        PriceChangeResponseDto priceChangeResponseDto = PriceChangeResponseDto.builder()
                .reducedPrice(priceChange.getPreviousPrice()-priceChange.getNewPrice())
                .newPrice(priceChange.getNewPrice())
                .changeOrder(priceChange.getChangeOrder())
                .changeDate(priceChange.getChangeDate())
                .build();
        return priceChangeResponseDto;
    }
}
