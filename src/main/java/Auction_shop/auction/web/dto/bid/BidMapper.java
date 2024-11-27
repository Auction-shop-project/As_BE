package Auction_shop.auction.web.dto.bid;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.product.Product;

public interface BidMapper {

    BidResponseDto toResponseDto(Bid bid, String profileImageUrl, int bidCount);
    MemberBidListResponseDto toMemberBidListResponseDto(Bid bid, Product product);
}
