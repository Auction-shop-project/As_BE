package Auction_shop.auction.web.dto.PriceChange;

import Auction_shop.auction.domain.priceChange.PriceChange;

public interface PriceChangeMapper {
    PriceChangeResponseDto toResponseDto (PriceChange priceChange);
}
