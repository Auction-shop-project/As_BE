package Auction_shop.auction.web.dto;

import Auction_shop.auction.domain.member.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String username;
    private String name;
    private Address address;
    private String phone;
    private Long point;
}
