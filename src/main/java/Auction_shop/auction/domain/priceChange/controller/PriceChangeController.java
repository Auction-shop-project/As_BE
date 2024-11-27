package Auction_shop.auction.domain.priceChange.controller;

import Auction_shop.auction.domain.priceChange.service.PriceChangeService;
import Auction_shop.auction.web.dto.PriceChange.PriceChangeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/priceChange")
public class PriceChangeController {
    private final PriceChangeService priceChangeService;

    @GetMapping("/{productId}")
    public ResponseEntity<List<PriceChangeResponseDto>> getPriceChangesByProductId(@PathVariable Long productId) {
        List<PriceChangeResponseDto> priceChanges = priceChangeService.getPriceChangeForProduct(productId);
        return ResponseEntity.ok(priceChanges);
    }
}
