package Auction_shop.auction.web.dto.member;

import Auction_shop.auction.domain.member.Member;

import java.util.List;

public interface MemberMapper {
    MemberResponseDto toResponseDto(Member member);
    MemberListResponseDto toListResponseDto(Member member);
}
