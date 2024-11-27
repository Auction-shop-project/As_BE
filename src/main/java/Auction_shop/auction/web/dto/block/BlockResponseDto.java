package Auction_shop.auction.web.dto.block;

import Auction_shop.auction.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDto {
    private Long id;;
    private Long BlockedMemberId;
    private String BlockedMemberName;
    private String BlockedMemberPhone;
    private List<Address> BlockedMemberAddresses;
}
