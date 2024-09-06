package Auction_shop.auction.domain.search.controller;

import Auction_shop.auction.domain.product.ProductDocument;
import Auction_shop.auction.domain.product.service.ProductService;
import Auction_shop.auction.domain.search.service.SearchService;
import Auction_shop.auction.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final ProductService productService;
    private final JwtUtil jwtUtil;

    /**
     * 상품 이름 검색
     */
    @GetMapping("/{title}")
    public ResponseEntity<Object> getByTitle(@RequestHeader("Authorization") String authorization, @PathVariable String title){
        Long memberId = jwtUtil.extractMemberId(authorization);
        searchService.saveSearchTerm(memberId, title);
        Iterable<ProductDocument> collect = productService.findByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }

    /**
     * 최근 검색어 불러오기
     */
    @GetMapping()
    public ResponseEntity<List<String>> getRecentSearches(@RequestHeader("Authorization") String authorization) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<String> collect = searchService.getRecentSearches(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(collect);
    }
}
