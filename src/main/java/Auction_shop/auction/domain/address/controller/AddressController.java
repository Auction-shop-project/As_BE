package Auction_shop.auction.domain.address.controller;

import Auction_shop.auction.domain.address.Address;
import Auction_shop.auction.domain.address.service.AddressService;
import Auction_shop.auction.security.jwt.JwtUtil;
import Auction_shop.auction.web.dto.address.AddressRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final JwtUtil jwtUtil;

    // 주소 추가
    @PostMapping()
    public ResponseEntity<Address> addAddress(@RequestHeader("Authorization") String authorization,
                                              @RequestBody AddressRequestDto addressRequestDto) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        Address address = addressService.addAddress(memberId, addressRequestDto);
        return ResponseEntity.ok(address);
    }

    // 주소 삭제 dafaultAddress인 경우 삭제 못하게 변경
    @DeleteMapping()
    public ResponseEntity<Void> deleteAddress(@RequestHeader("Authorization") String authorization,
                                              @RequestBody List<Long> deleteList) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        addressService.deleteAddresses(memberId, deleteList);
        return ResponseEntity.noContent().build();
    }

    // 주소 수정
    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@RequestHeader("Authorization") String authorization,
                                              @PathVariable Long addressId,
                                              @RequestBody AddressRequestDto addressRequestDto) {
        Long memberId = jwtUtil.extractMemberId(authorization);
        Address address = addressService.updateAddress(memberId, addressId, addressRequestDto);
        return ResponseEntity.ok(address);
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
