package Auction_shop.auction.web.dto.like;

import Auction_shop.auction.domain.product.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeListResponseDto {

    private Long product_id;
    private String title;
    private boolean isSold;
    private String conditions;
    private Set<String> categories;
    private Set<String> tradeTypes;
    private String tradeLocation;
    private int current_price;
    private int initial_price;
    private String imageUrl;
    private String createdBy;
    private int likeCount;
    private boolean isLiked;
    private ProductType productType;
    private int bidCount;
}
