package Auction_shop.auction.web.dto.block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockCreateRequestDto {
    private Long blockedMemberId;
}
