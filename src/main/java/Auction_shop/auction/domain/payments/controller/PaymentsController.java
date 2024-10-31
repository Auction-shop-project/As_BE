package Auction_shop.auction.domain.payments.controller;

import Auction_shop.auction.domain.alert.AlertType;
import Auction_shop.auction.domain.alert.util.AlertUtil;
import Auction_shop.auction.domain.member.Member;
import Auction_shop.auction.domain.payments.service.DescendingPaymentsService;
import Auction_shop.auction.domain.product.repository.ProductJpaRepository;
import Auction_shop.auction.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final DescendingPaymentsService descendingPaymentsService;
    private final ProductJpaRepository productRepository;
    private final JwtUtil jwtUtil;
    private final AlertUtil alertUtil;

    @PostMapping("/{productId}/{impUid}")
    public  ResponseEntity<Map<String, Object>> createPayments(@RequestHeader("Authorization") String authorization,
                                                   @PathVariable Long productId,
                                                   @PathVariable String impUid){
        Long memberId = jwtUtil.extractMemberId(authorization);
        ResponseEntity<Map<String, Object>>  collect;
        try {
            collect = descendingPaymentsService.PaymentsVerify(impUid, productId, memberId);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "결제 검증 중 오류 발생");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        Member member = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(productId + "에 해당하는 물품이 없습니다."))
                .getMember();

        alertUtil.run(member.getId(), member.getNickname(), "새로운 입찰", AlertType.newBid);
        return collect;

    }
}
