package Auction_shop.auction.domain.alert.controller;

import Auction_shop.auction.domain.alert.Alert;
import Auction_shop.auction.domain.alert.service.AlertService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.alert.AlertDeleteRequestDto;
import Auction_shop.auction.web.dto.alert.AlertListResponseDto;
import Auction_shop.auction.web.dto.alert.AlertMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;
    private final AlertMapper alertMapper;
    private final JwtUtil jwtUtil;

    @GetMapping("/alerts")
    public ResponseEntity<AlertListResponseDto> getList(@RequestHeader("Authorization") String authorization){
        Long memberId = jwtUtil.extractMemberId(authorization);
        List<Alert> alerts = alertService.getList(memberId);
        AlertListResponseDto collect = alertMapper.toListResponseDto(alerts);
        return ResponseEntity.ok(collect);
    }

    @DeleteMapping("/alert")
    public ResponseEntity<Void> delete(@RequestBody AlertDeleteRequestDto alertDeleteRequestDto){
        alertService.delete(alertDeleteRequestDto.getId());
        return ResponseEntity.noContent().build();
    }
}
