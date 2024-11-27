package Auction_shop.auction.web.dto.notice;

import Auction_shop.auction.domain.notice.Notice;

public interface NoticeMapper {
    NoticeResponseDto toResponseDto(Notice notice);
    NoticeListResponseDto toListResponseDto(Notice notice);
}
