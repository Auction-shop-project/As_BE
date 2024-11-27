package Auction_shop.auction.security.auth.controller;

import Auction_shop.auction.security.auth.service.AuthService;
import Auction_shop.auction.web.dto.ReissueDto;
import Auction_shop.auction.web.dto.auth.AuthRequestDto;
import Auction_shop.auction.web.dto.auth.AuthResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(HttpServletResponse response,
                                                @RequestBody AuthRequestDto authRequestDto) {
        AuthResponseDto collect = authService.login(authRequestDto, response);
        return ResponseEntity.ok(collect);
    }
    

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(HttpServletResponse response,
                                                   @RequestBody ReissueDto reissueDto) {
        AuthResponseDto collect = authService.refresh(reissueDto, response);
        return ResponseEntity.ok(collect);
    }
}
