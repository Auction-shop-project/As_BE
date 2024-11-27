package Auction_shop.auction.web.dto.member;

import Auction_shop.auction.domain.image.Image;
import Auction_shop.auction.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberMapperImpl implements MemberMapper{
    @Override
    public MemberResponseDto toResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUuid())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .address(member.getAddresses())
                .phone(member.getPhone())
                .point(member.getPoint())
                .profileImageUrl(Optional.ofNullable(member.getProfileImage())
                        .map(Image::getAccessUrl)
                        .orElse(null))
                .categories(member.getCategories())
                .role(member.getRole())
                .available(member.isAvailable())
                .build();
    }

    @Override
    public MemberListResponseDto toListResponseDto(Member member) {
        return MemberListResponseDto.builder()
                .id(member.getId())
                .username(member.getUuid())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .address(member.getAddresses())
                .phone(member.getPhone())
                .point(member.getPoint())
                .profileImageUrl(Optional.ofNullable(member.getProfileImage())
                        .map(Image::getAccessUrl)
                        .orElse(null))
                .categories(member.getCategories())
                .role(member.getRole())
                .available(member.isAvailable())
                .build();
    }
}
