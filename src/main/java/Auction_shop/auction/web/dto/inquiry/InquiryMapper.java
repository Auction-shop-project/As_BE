package Auction_shop.auction.web.dto.inquiry;

import Auction_shop.auction.domain.inquriy.Inquiry;
import Auction_shop.auction.domain.member.Member;

public interface InquiryMapper {

    InquiryResponseDto toResponseDto(Inquiry inquiry);
    InquiryListResponseDto toListResponseDto(Inquiry inquiry);
    Inquiry toEntity(InquiryCreateDto inquiryDto, Member member);
}
