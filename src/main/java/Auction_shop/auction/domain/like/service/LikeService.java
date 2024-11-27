package Auction_shop.auction.domain.like.service;

import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.web.dto.like.LikeResponseDto;

import java.util.List;

public interface LikeService {
    Like addProductToLike(Long memberId, Long productId);
    List<Like> getLikeList(Long memberId);
    List<Long> getLikeItems(Long memberId);
    boolean isLiked(Long memberId, Long productId);
    void removeProductFromLike(Long memberId, Long productId);
    int getProductLikeCount(Long productId);
}
