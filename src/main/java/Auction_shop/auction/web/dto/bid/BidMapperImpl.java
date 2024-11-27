package Auction_shop.auction.web.dto.bid;

import Auction_shop.auction.domain.bid.Bid;
import Auction_shop.auction.domain.member.repository.MemberRepository;
import Auction_shop.auction.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BidMapperImpl implements BidMapper {

    private final MemberRepository memberRepository;

    @Override
    public BidResponseDto toResponseDto(Bid bid, String profileImageUrl, int bidCount) {
        BidResponseDto bidResponseDto = BidResponseDto.builder()
                .amount(bid.getAmount())
                .profileImageUrl(profileImageUrl)
                .bidTime(bid.getBidTime())
                .bidCount(bidCount)
                .bidStatus(bid.getBidStatus())
                .build();
        return bidResponseDto;
    }

    @Override
    public MemberBidListResponseDto toMemberBidListResponseDto(Bid bid, Product product) {

        MemberBidListResponseDto responseDto = MemberBidListResponseDto.builder()
                .productId(bid.getProductId())
                .imageUrl(product.getImageUrls().stream().findFirst().orElse(null))
                .productType(product.getProductType().name())
                .initial_price(product.getInitial_price())
                .current_price(product.getCurrent_price())
                .title(product.getTitle())
                .bidStatus(bid.getBidStatus())
                .bidTime(bid.getBidTime())
                .amount(bid.getAmount())
                .build();
        return responseDto;
    }
}
