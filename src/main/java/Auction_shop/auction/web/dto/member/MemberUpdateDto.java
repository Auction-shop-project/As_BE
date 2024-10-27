package Auction_shop.auction.web.dto.member;

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
public class MemberUpdateDto {
    private String name;
    private String nickname;
    private String email;
    private String phone;
    private String address;
    private String detailAddress;
    private String deviceToken;
    private String zipcode;
    private Set<String> categories;
    private boolean changeImage;
}
