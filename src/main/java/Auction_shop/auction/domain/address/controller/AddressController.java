package Auction_shop.auction.domain.address.controller;

import Auction_shop.auction.domain.address.service.AddressService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.address.AddressRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final JwtUtil jwtUtil;

    // 주소 추가
    @PostMapping()
    public ResponseEntity<Void> addAddress(@RequestHeader("Authorization") String authorization,
                                           @RequestBody AddressRequestDto addressRequestDto) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        addressService.addAddress(memberId, addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 주소 삭제 dafaultAddress인 경우 삭제 못하게 변경
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@RequestHeader("Authorization") String authorization,
                                              @PathVariable Long addressId) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        addressService.deleteAddress(memberId, addressId);
        return ResponseEntity.noContent().build();
    }

    // 주소 수정
    @PutMapping("/{addressId}")
    public ResponseEntity<Void> updateAddress(@RequestHeader("Authorization") String authorization,
                                              @PathVariable Long addressId,
                                              @RequestBody AddressRequestDto addressRequestDto) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        addressService.updateAddress(memberId, addressId, addressRequestDto);
        return ResponseEntity.ok().build();
    }

    // 기본 주소지 설정
    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> updateDefaultAddress(@RequestHeader("Authorization") String authorization,
                                                     @PathVariable Long addressId) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        addressService.updateDefaultAddress(memberId, addressId);
        return ResponseEntity.ok().build();
    }
}