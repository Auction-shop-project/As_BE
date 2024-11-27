package Auction_shop.auction.web.dto.like;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.product.Product;
import org.springframework.stereotype.Component;

@Component
public class LikeMapperImpl implements LikeMapper{

    @Override
    public LikeResponseDto toResponseDto(Like like) {
        LikeResponseDto likeResponseDto = LikeResponseDto.builder()
                .nickname(like.getMember().getNickname())
                .productTitle(like.getProduct().getTitle())
                .build();
        return likeResponseDto;
    }

    @Override
    public LikeListResponseDto toListResponseDto(Like like) {

        Product product = like.getProduct();
        String imageUrl = null;

        if(!product.getImageList().isEmpty()){
            imageUrl = product.getImageUrls().get(0);
        }

        LikeListResponseDto responseDto = LikeListResponseDto.builder()
                .product_id(product.getId())
                .title(product.getTitle())
                .conditions(product.getConditions())
                .categories(product.getCategories())
                .tradeTypes(product.getTradeTypes())
                .initial_price(product.getInitial_price())
                .current_price(product.getCurrent_price())
                .tradeLocation(product.getTradeLocation())
                .createdBy(product.getCreatedBy())
                .likeCount(product.getLikeCount())
                .imageUrl(imageUrl)
                .bidCount(product.getBidCount())
                .productType(product.getProductType())
                .isSold(product.isSold())
                .isLiked(true)
                .build();

        return responseDto;
    }
}
