package Auction_shop.auction.domain.search.controller;

import Auction_shop.auction.domain.like.service.LikeService;
import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.search.service.SearchRepository;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.product.ProductListResponseDto;
import Auction_shop.auction.web.dto.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchRepository searchRepository;
    private final ProductService productService;
    private final LikeService likeService;
    private final ProductMapper productMapper;
    private final JwtUtil jwtUtil;

    /**
     * 상품 이름 검색
     */
    @GetMapping("/{title}")
    public ResponseEntity<Object> getByTitle(@RequestHeader("Authorization") String authorization, @PathVariable String title){
        Long memberId = jwtUtil.extractMemberId(authorization);
        searchRepository.saveSearchTerm(memberId, title);

        List<Long> likedProductsIds = likeService.getLikeItems(memberId);

        Iterable<ProductDocument> products = productService.findByTitleLike(title);

        List<ProductListResponseDto> collect = StreamSupport.stream(products.spliterator(), false)
                .map(product -> productMapper.toListResponseDto(product, likedProductsIds.contains(product.getId())))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    /**
     * 최근 검색어 불러오기
     */
    @GetMapping()
    public ResponseEntity<List<String>> getRecentSearches(@RequestHeader("Authorization") String authorization) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<String> collect = searchRepository.getRecentSearches(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }
}
