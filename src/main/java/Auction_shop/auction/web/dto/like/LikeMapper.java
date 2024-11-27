package Auction_shop.auction.web.dto.like;

import Auction_shop.auction.domain.like.Like;

public interface LikeMapper {

    LikeResponseDto toResponseDto(Like like);
    LikeListResponseDto toListResponseDto(Like like);
}
