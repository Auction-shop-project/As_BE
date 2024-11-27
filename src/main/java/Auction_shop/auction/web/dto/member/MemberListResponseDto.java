package Auction_shop.auction.web.dto.member;

import Auction_shop.auction.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberListResponseDto {
    private Long id;
    private String username;
    private String name;
    private String nickname;
    private String email;
    private List<Address> address;
    private String phone;
    private Long point;
    private boolean available;
    private Set<String> categories;
    private String profileImageUrl;
    private String role;
}
