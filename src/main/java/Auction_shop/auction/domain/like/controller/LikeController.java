package Auction_shop.auction.domain.like.controller;

import Auction_shop.auction.domain.alert.service.AlertService;
import Auction_shop.auction.domain.alert.util.AlertUtil;
import Auction_shop.auction.domain.like.Like;
import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.like.LikeCreateDto;
import Auction_shop.auction.web.dto.like.LikeListResponseDto;
import Auction_shop.auction.web.dto.like.LikeMapper;
import Auction_shop.auction.web.dto.like.LikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;
    private final AlertService alertService;
    private final LikeMapper likeMapper;
    private final JwtUtil jwtUtil;
    private final AlertUtil alertUtil;

    @PostMapping
    public ResponseEntity<LikeResponseDto> addProductToLike(@RequestHeader("Authorization") String authorization, @RequestBody LikeCreateDto likeCreateDto){
        Long memberId = jwtUtil.extractMemberId(authorization);
        Like like = likeService.addProductToLike(memberId, likeCreateDto.getProductId());
        alertService.add(memberId, "AddLike");
        alertUtil.run(likeCreateDto.getMemberId(), "addLike");
        LikeResponseDto collect = likeMapper.toResponseDto(like);
        return ResponseEntity.ok(collect);
    }

    @GetMapping()
    public ResponseEntity<List<LikeListResponseDto>> getLikeList(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<Like> likes = likeService.getLikeList(memberId);

        if (likes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<LikeListResponseDto> collect = likes.stream()
                .map(likeMapper::toListResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @DeleteMapping("/{productId}")
    public void removeProductFromLike(@RequestHeader("Authorization") String authorization, @PathVariable Long productId){
        Long memberId = jwtUtil.extractMemberId(authorization);
        likeService.removeProductFromLike(memberId, productId);
    }

    //물건 별 좋아요 갯수 확인 api
    @GetMapping("/product/{productId}/likeCount")
    public int getProductLikeCount(@PathVariable Long productId){
        return likeService.getProductLikeCount(productId);
    }
}
